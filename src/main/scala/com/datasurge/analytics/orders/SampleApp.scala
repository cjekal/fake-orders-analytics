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
      config("es.index.auto.create", "true").
      config("es.read.metadata", "true").
      getOrCreate()
    spark.sparkContext.setLogLevel("WARN")

    val esDF = spark.esDF("kindas")
    esDF.printSchema()
    esDF.show(false)

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
