# fake-orders-analytics

## Instructions
```sbtshell
assembly
```

```shell script
docker-compose up -d
cp target/scala-2.12/fake-order-analytics-assembly-0.1.jar data/spark
docker-compose exec spark-master /spark/bin/spark-submit --packages "org.postgresql:postgresql:42.2.5" --class com.datasurge.analytics.orders.SampleApp /tmp/spark/fake-order-analytics-assembly-0.1.jar
```
