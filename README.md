# fake-orders-analytics

## Instructions
```sbtshell
assembly
```

```shell script
cp target/scala-2.12/fake-order-analytics-assembly-0.1.jar data/spark
```

```shell script
/spark/bin/spark-submit --packages "org.postgresql:postgresql:42.2.5" --class com.datasurge.analytics.orders.SampleApp /tmp/spark/fake-order-analytics-assembly-0.1.jar
```



Added username "cheese" with password "sdj4tbueg_3T1f"


docker build -t tabpy-serve .
docker tag tabpy-serve gcr.io/sbir-training/tabpy-serve:latest
docker push gcr.io/sbir-training/tabpy-serve:latest
gcloud run deploy tabpy-serve --project "sbir-training" --image gcr.io/sbir-training/tabpy-serve:latest --region "us-west1" --platform managed --memory 4G --allow-unauthenticated --port=9004 --set-env-vars=TABPY_USER=cheese,TABPY_PASSWORD=sdj4tbueg_3T1f



SCRIPT_REAL("
return tab_py.query('tf_idf_cosine_similarity', _arg1, _arg2)['response']
", ATTR([First Forename]), [Search First Forename]) < 0.5