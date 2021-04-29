package com.datasurge.analytics.orders

import java.util.Properties

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.apache.spark.sql.SparkSession
import scalaj.http.Http

object SampleApp {
  def main(args: Array[String]) {
    val spark = SparkSession.builder.appName("Sample Application").getOrCreate()

    import spark.implicits._

    val ordersDF = spark.read.option("header", "true").csv("/tmp/spark/orders_history.csv")
    ordersDF.show()

    val props = new Properties()
    props.put("user", "postgres")
    props.put("password", "test")
    val customersDF = spark.read.option("driver", "org.postgresql.Driver").jdbc("jdbc:postgresql://db:5432/postgres", "public.customers", props)
    customersDF.show()

    val productCategoriesDF = spark.read.option("driver", "org.postgresql.Driver").jdbc("jdbc:postgresql://db:5432/postgres", "public.product_categories", props)
    productCategoriesDF.show()

    val preDF = ordersDF.join(customersDF, "customer_id").join(productCategoriesDF, "product_category_id")
    preDF.show()
    preDF.printSchema()

    val df = preDF.map(record => {
      val mapper = new ObjectMapper()
      mapper.registerModule(DefaultScalaModule)
      val json = Http(s"http://api/lumber/${record.getString(2)}").asString.body
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

    df.show()
    df.printSchema()

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
