package com.datasurge.analytics.orders

import org.apache.spark.sql.SparkSession

object SampleApp {
  def main(args: Array[String]) {
    val spark = SparkSession.builder.appName("Sample Application").getOrCreate()
    val df = spark.read.option("header", true).csv("/tmp/spark/orders_history.csv")
    df.show()
    spark.stop()
  }
}
