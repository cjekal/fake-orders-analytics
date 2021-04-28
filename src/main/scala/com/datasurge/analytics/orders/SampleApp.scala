package com.datasurge.analytics.orders

import java.util.Properties

import org.apache.spark.sql.SparkSession

object SampleApp {
  def main(args: Array[String]) {
    val spark = SparkSession.builder.appName("Sample Application").getOrCreate()
    val df = spark.read.option("header", "true").csv("/tmp/spark/orders_history.csv")
    df.show()

    val props = new Properties()
    props.put("user", "postgres")
    props.put("password", "test")
    val customersDF = spark.read.option("driver", "org.postgresql.Driver").jdbc("jdbc:postgresql://db:5432/postgres", "public.customers", props)
    customersDF.show()

    val productCategoriesDF = spark.read.option("driver", "org.postgresql.Driver").jdbc("jdbc:postgresql://db:5432/postgres", "public.product_categories", props)
    productCategoriesDF.show()

    df.join(customersDF, "customer_id").join(productCategoriesDF, "product_category_id").show()

    spark.stop()
  }
}
