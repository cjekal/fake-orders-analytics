package com.datasurge.analytics.orders

import java.util.Properties

import org.apache.spark.sql.SparkSession
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
    val hcpcsDF = spark.read.option("driver", "org.postgresql.Driver").jdbc("jdbc:postgresql://db:5432/postgres", "public.hcpcs_codes", props)
    hcpcsDF.createOrReplaceTempView("hcpcs_codes")

    val denormDF = spark.sql(
      """
        |select
        | d.beneficiary_id,
        | d.claim_number,
        | d.line_number,
        | date_format(cast(d.claim_date as timestamp), "yyyy-MM-dd") as claim_date,
        | d.claim_type,
        | d.performing_pin_number,
        | d.performing_physician_npi,
        | d.provider_type,
        | d.provider_state,
        | d.provider_specialty,
        | d.cms_type_service,
        | d.place_of_service,
        | date_format(cast(d.last_expense_date as timestamp), "yyyy-MM-dd") as last_expense_date,
        | d.hcpcs_code,
        | h.SHORT_DESCRIPTION as hcpcs_code_description,
        | d.payment_amount,
        | d.bene_payment_amount,
        | d.provider_payment_amount,
        | d.bene_part_b_deductible_amount,
        | d.bene_primary_payer_paid_amount,
        | d.coinsurance_amount,
        | d.submitted_charged_amount,
        | d.allowed_charged_amount,
        | d.diagnosis_code,
        | d.diagnosis,
        | d.clinical_lab_charge_amount,
        | d.other_applied_amount1,
        | d.other_applied_amount2,
        | d.other_applied_amount3,
        | d.other_applied_amount4,
        | d.other_applied_amount5,
        | d.other_applied_amount6,
        | d.other_applied_amount7,
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
        | date_format(cast(b.date_of_death as timestamp), "yyyy-MM-dd") as date_of_death,
        | b.medicare_medicaid_status
        |from details d
        | join claims c
        |   on d.claim_number = c.claim_number
        | join beneficiaries b
        |   on d.beneficiary_id = b.beneficiary_id
        | left join hcpcs_codes h
        |   on d.hcpcs_code = h.HCPC
        |""".stripMargin)
    denormDF.createOrReplaceTempView("denorm")

    denormDF.write.parquet("/tmp/spark/output")
    denormDF.saveToEs("claims")

    spark.stop()
  }
}
