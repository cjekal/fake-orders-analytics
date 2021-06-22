# fake-orders-analytics

## Instructions
```sbtshell
assembly
```

```kibana
PUT /claims
{
  "mappings": 
  {
    "properties" : {
      "age" : {
        "type" : "long"
      },
      "age_range" : {
        "type" : "text",
        "fields" : {
          "keyword" : {
            "type" : "keyword",
            "ignore_above" : 256
          }
        }
      },
      "allowed_charged_amount" : {
        "type" : "float"
      },
      "bene_part_b_deductible_amount" : {
        "type" : "float"
      },
      "bene_payment_amount" : {
        "type" : "float"
      },
      "bene_primary_payer_paid_amount" : {
        "type" : "float"
      },
      "beneficiary_county" : {
        "type" : "text",
        "fields" : {
          "keyword" : {
            "type" : "keyword",
            "ignore_above" : 256
          }
        }
      },
      "beneficiary_id" : {
        "type" : "long"
      },
      "beneficiary_payment_amount" : {
        "type" : "float"
      },
      "beneficiary_state" : {
        "type" : "text",
        "fields" : {
          "keyword" : {
            "type" : "keyword",
            "ignore_above" : 256
          }
        }
      },
      "carrier_number" : {
        "type" : "long"
      },
      "claim_allowed_amount" : {
        "type" : "float"
      },
      "claim_cash_deductible_applied_amount" : {
        "type" : "float"
      },
      "claim_county" : {
        "type" : "text",
        "fields" : {
          "keyword" : {
            "type" : "keyword",
            "ignore_above" : 256
          }
        }
      },
      "claim_date" : {
        "type" : "date"
      },
      "claim_number" : {
        "type" : "long"
      },
      "claim_payment_amount" : {
        "type" : "float"
      },
      "claim_provider_payment_amount" : {
        "type" : "float"
      },
      "claim_state" : {
        "type" : "text",
        "fields" : {
          "keyword" : {
            "type" : "keyword",
            "ignore_above" : 256
          }
        }
      },
      "claim_submitted_amount" : {
        "type" : "float"
      },
      "claim_type" : {
        "type" : "text",
        "fields" : {
          "keyword" : {
            "type" : "keyword",
            "ignore_above" : 256
          }
        }
      },
      "clinical_lab_charge_amount" : {
        "type" : "float"
      },
      "cms_type_service" : {
        "type" : "text",
        "fields" : {
          "keyword" : {
            "type" : "keyword",
            "ignore_above" : 256
          }
        }
      },
      "coinsurance_amount" : {
        "type" : "float"
      },
      "date_of_death" : {
        "type" : "date"
      },
      "diagnosis" : {
        "type" : "text",
        "fields" : {
          "keyword" : {
            "type" : "keyword",
            "ignore_above" : 256
          }
        }
      },
      "diagnosis_code" : {
        "type" : "text",
        "fields" : {
          "keyword" : {
            "type" : "keyword",
            "ignore_above" : 256
          }
        }
      },
      "disposition" : {
        "type" : "text",
        "fields" : {
          "keyword" : {
            "type" : "keyword",
            "ignore_above" : 256
          }
        }
      },
      "gender" : {
        "type" : "text",
        "fields" : {
          "keyword" : {
            "type" : "keyword",
            "ignore_above" : 256
          }
        }
      },
      "hcpcs_code" : {
        "type" : "text",
        "fields" : {
          "keyword" : {
            "type" : "keyword",
            "ignore_above" : 256
          }
        }
      },
      "hcpcs_code_description" : {
        "type" : "text",
        "fields" : {
          "keyword" : {
            "type" : "keyword",
            "ignore_above" : 256
          }
        }
      },
      "last_expense_date" : {
        "type" : "date"
      },
      "line_number" : {
        "type" : "long"
      },
      "medicare_medicaid_status" : {
        "type" : "text",
        "fields" : {
          "keyword" : {
            "type" : "keyword",
            "ignore_above" : 256
          }
        }
      },
      "other_applied_amount1" : {
        "type" : "float"
      },
      "other_applied_amount2" : {
        "type" : "float"
      },
      "other_applied_amount3" : {
        "type" : "float"
      },
      "other_applied_amount4" : {
        "type" : "float"
      },
      "other_applied_amount5" : {
        "type" : "float"
      },
      "other_applied_amount6" : {
        "type" : "float"
      },
      "other_applied_amount7" : {
        "type" : "float"
      },
      "payment_amount" : {
        "type" : "float"
      },
      "payment_denial_code" : {
        "type" : "text",
        "fields" : {
          "keyword" : {
            "type" : "keyword",
            "ignore_above" : 256
          }
        }
      },
      "performing_physician_npi" : {
        "type" : "long"
      },
      "performing_pin_number" : {
        "type" : "text",
        "fields" : {
          "keyword" : {
            "type" : "keyword",
            "ignore_above" : 256
          }
        }
      },
      "place_of_service" : {
        "type" : "text",
        "fields" : {
          "keyword" : {
            "type" : "keyword",
            "ignore_above" : 256
          }
        }
      },
      "principal_diagnosis" : {
        "type" : "text",
        "fields" : {
          "keyword" : {
            "type" : "keyword",
            "ignore_above" : 256
          }
        }
      },
      "principal_diagnosis_code" : {
        "type" : "text",
        "fields" : {
          "keyword" : {
            "type" : "keyword",
            "ignore_above" : 256
          }
        }
      },
      "provider_payment_amount" : {
        "type" : "float"
      },
      "provider_specialty" : {
        "type" : "text",
        "fields" : {
          "keyword" : {
            "type" : "keyword",
            "ignore_above" : 256
          }
        }
      },
      "provider_state" : {
        "type" : "text",
        "fields" : {
          "keyword" : {
            "type" : "keyword",
            "ignore_above" : 256
          }
        }
      },
      "provider_type" : {
        "type" : "text",
        "fields" : {
          "keyword" : {
            "type" : "keyword",
            "ignore_above" : 256
          }
        }
      },
      "race" : {
        "type" : "text",
        "fields" : {
          "keyword" : {
            "type" : "keyword",
            "ignore_above" : 256
          }
        }
      },
      "reason_for_entitlement" : {
        "type" : "text",
        "fields" : {
          "keyword" : {
            "type" : "keyword",
            "ignore_above" : 256
          }
        }
      },
      "reference_year" : {
        "type" : "long"
      },
      "referring_physician_npi" : {
        "type" : "long"
      },
      "referring_physician_upin" : {
        "type" : "text",
        "fields" : {
          "keyword" : {
            "type" : "keyword",
            "ignore_above" : 256
          }
        }
      },
      "submitted_charged_amount" : {
        "type" : "float"
      }
    }
  }
}
```

```shell script
cp target/scala-2.12/fake-order-analytics-assembly-0.1.jar data/spark/
```

```shell script
docker-compose exec spark-master /spark/bin/spark-submit --packages "org.postgresql:postgresql:42.2.5" --class com.datasurge.analytics.orders.SampleApp /tmp/spark/fake-order-analytics-assembly-0.1.jar
```
