package com.datasurge.analytics.orders

import java.text.SimpleDateFormat
import java.util.{Calendar, Properties}

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.apache.spark.ml.regression.IsotonicRegression
import org.apache.spark.sql.SparkSession
import scalaj.http.Http

object SampleApp {
  def main(args: Array[String]) {
    val spark = SparkSession.builder.appName("Sample Application").getOrCreate()
    spark.sparkContext.setLogLevel("WARN")

    import spark.implicits._

    val ordersDF = spark.read.option("header", "true").csv("/tmp/spark/orders_history.csv")

    val props = new Properties()
    props.put("user", "postgres")
    props.put("password", "test")
    val customersDF = spark.read.option("driver", "org.postgresql.Driver").jdbc("jdbc:postgresql://db:5432/postgres", "public.customers", props)

    val productCategoriesDF = spark.read.option("driver", "org.postgresql.Driver").jdbc("jdbc:postgresql://db:5432/postgres", "public.product_categories", props)

    val preDF = ordersDF.join(customersDF, "customer_id").join(productCategoriesDF, "product_category_id")

    val df = preDF.map(record => {
      val mapper = new ObjectMapper()
      mapper.registerModule(DefaultScalaModule)
      var lumberDate = record.getString(2)
      var json = Http(s"http://api/lumber/${lumberDate}").asString.body
      val dateFormat = new SimpleDateFormat("yyyy-MM-dd")
      while (json == "" || json == "{}") {
        val calendar = Calendar.getInstance()
        calendar.setTime(dateFormat.parse(lumberDate))
        calendar.add(Calendar.DATE, -1)
        lumberDate = dateFormat.format(calendar.getTime)
        json = Http(s"http://api/lumber/${lumberDate}").asString.body
      }
      val lumberFutures = mapper.readValue(json, classOf[LumberFutures])
      (
        record.getString(2),
        record.getString(3).toLong,
        record.getString(4).toDouble,
        record.getString(5).toDouble,
        record.getString(6),
        record.getString(7),
        record.getString(8),
        record.getString(9),
        record.getInt(10),
        lumberFutures.price,
        lumberFutures.open,
        lumberFutures.high,
        lumberFutures.low,
        parseVolume(lumberFutures.volume),
        parseChangeInPct(lumberFutures.changeInPct)
      )
    }).toDF("order_date", "order_qty", "cost_per_unit", "price_per_unit", "customer_name", "customer_address", "customer_shipping_address", "product_category_name", "product_category_quality_tier", "lumber_futures_price", "lumber_futures_open", "lumber_futures_high", "lumber_futures_low", "lumber_futures_volume", "lumber_futures_pct_change")

    val model = new IsotonicRegression()
    model.setFeaturesCol("lumber_futures_price")
    model.setLabelCol("cost_per_unit")
    model.setPredictionCol("predicted_cost_per_unit")
    model.setIsotonic(true)

    val modelOutput = model.fit(df)

    println(s"Boundaries in increasing order: ${modelOutput.boundaries}\n")
    println(s"Predictions associated with the boundaries: ${modelOutput.predictions}\n")

    val dfWithPredictions = modelOutput.transform(df)
    dfWithPredictions.show()

    spark.stop()
  }

  def parseVolume(volume: String) = {
    if (volume.endsWith("K")) {
      (volume.replace("K", "").toDouble * 1000).toLong
    }
    else if (volume.endsWith("M")) {
      (volume.replace("M", "").toDouble * 1000 * 1000).toLong
    }
    else {
      0L
    }
  }

  def parseChangeInPct(changeInPct: String) = changeInPct.replace("%", "").toDouble / 100
}

case class LumberFutures(
  id: String,
  price: Double,
  open: Double,
  high: Double,
  low: Double,
  volume: String,
  @JsonProperty("change_in_pct") changeInPct: String
)
