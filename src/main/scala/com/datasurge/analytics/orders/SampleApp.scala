package com.datasurge.analytics.orders

import java.text.SimpleDateFormat
import java.util.{Calendar, Properties}

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.apache.spark.ml.regression.IsotonicRegression
import org.apache.spark.sql.SparkSession
import scalaj.http.Http
import org.elasticsearch.spark.sql._

object SampleApp {
  def main(args: Array[String]) {
    val spark = SparkSession.
      builder.
      appName("Sample Application").
      config("spark.es.nodes", "elasticsearch").
      config("spark.es.port", "9200").
      getOrCreate()
    spark.sparkContext.setLogLevel("WARN")

    import spark.implicits._

    val detailsDF = spark.read.
      option("header", "true").
      option("sep", "\t").
      option("inferSchema", "true").
      csv("/tmp/spark/demo_data/claim_service.csv")
    val claimsDF = spark.read.
      option("header", "true").
      option("sep", "\t").
      option("inferSchema", "true").
      csv("/tmp/spark/demo_data/claim.csv")
    val beneficiariesDF = spark.read.
      option("header", "true").
      option("sep", "\t").
      option("inferSchema", "true").
      csv("/tmp/spark/demo_data/beneficiary.csv")

    detailsDF.createOrReplaceTempView("details")
    claimsDF.createOrReplaceTempView("claims")
    beneficiariesDF.createOrReplaceTempView("beneficiaries")

    val props = new Properties()
    props.put("user", "postgres")
    props.put("password", "test")
    val customersDF = spark.read.option("driver", "org.postgresql.Driver").jdbc("jdbc:postgresql://db:5432/postgres", "public.customers", props)
    customersDF.createOrReplaceTempView("customers")

    val productCategoriesDF = spark.read.option("driver", "org.postgresql.Driver").jdbc("jdbc:postgresql://db:5432/postgres", "public.product_categories", props)
    productCategoriesDF.createOrReplaceTempView("product_categories")

    val denormDF = spark.sql(
      """
        |select
        | d.*,
        | c.disposition,
        | c.carrier_number,
        | c.payment_denial_code,
        | c.claim_payment_amount,
        | c.referring_physician_npi,
        | c.referring_physician_upin,
        | c.provider_payment_amount as claim_provider_payment_amount,
        | c.beneficiary_payment_amount,
        | c.claim_submitted_amount,
        | c.claim_allowed_amount,
        | c.claim_cash_deductible_applied_amount,
        | c.principal_diagnosis_code,
        | c.principal_diagnosis,
        | c.age_range,
        | c.county as claim_county,
        | c.state as claim_state,
        | b.reference_year,
        | b.state as beneficiary_state,
        | b.county as beneficiary_county,
        | b.gender,
        | b.race,
        | b.age,
        | b.reason_for_entitlement,
        | b.date_of_death,
        | b.medicare_medicaid_status
        |from details d
        | join claims c
        |   on d.claim_number = c.claim_number
        | join beneficiaries b
        |   on d.beneficiary_id = b.beneficiary_id
        |""".stripMargin)
    denormDF.createOrReplaceTempView("denorm")

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
        record.getString(0),
        record.getString(1),
        record.getString(2),
        (dateFormat.parse(record.getString(2)).getTime / 1000 / 60 / 60 / 24).toDouble,
        record.getDouble(3),
        record.getDouble(4),
        record.getDouble(5),
        record.getDouble(6),
        record.getDouble(7),
        lumberFutures.price,
        lumberFutures.open,
        lumberFutures.high,
        lumberFutures.low,
        parseVolume(lumberFutures.volume),
        parseChangeInPct(lumberFutures.changeInPct)
      )
    }).toDF("customer_name", "product_category_name", "order_date", "order_epoch_time", "order_price", "order_cost", "order_qty", "price_per_unit", "cost_per_unit", "lumber_futures_price", "lumber_futures_open", "lumber_futures_high", "lumber_futures_low", "lumber_futures_volume", "lumber_futures_pct_change")

    val model = new IsotonicRegression()
    model.setFeaturesCol("order_epoch_time")
    model.setLabelCol("cost_per_unit")
    model.setPredictionCol("predicted_cost_per_unit")
    model.setIsotonic(true)

    val modelOutput = model.fit(df)

    println(s"Boundaries in increasing order: ${modelOutput.boundaries}\n")
    println(s"Predictions associated with the boundaries: ${modelOutput.predictions}\n")

    val dfWithPredictions = modelOutput.transform(df)
    dfWithPredictions.saveToEs("orders")

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
