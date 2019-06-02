
"""
 Processes direct stream from kafka, '\n' delimited text directly received
   every 2 seconds.
 Usage: kafka-direct-iotmsg.py <broker_list> <topic>

 To run this on your local machine, you need to setup Kafka and create a
   producer first, see:
 http://kafka.apache.org/documentation.html#quickstart

 and then run the example
    `$ bin/spark-submit --jars \
      external/kafka-assembly/target/scala-*/spark-streaming-kafka-assembly-*.jar \
      kafka-direct-iotmsg.py \
      localhost:9092 iotmsgs`
"""
#from __future__ import print_function

import sys
import re

#from pyspark import SparkContext
from pyspark.sql import SparkSession
#from pyspark.streaming import StreamingContext
from pyspark.streaming.kafka import KafkaUtils

from operator import add

from pyspark.sql.types import *
from pyspark.sql.functions import *

if __name__ == "__main__":
    #if len(sys.argv) != 3:
    #    print("Usage: kafka-direct-iotmsg.py <broker_list> <topic>", file=sys.stderr)
    #    exit(-1)

    #sc = SparkContext(appName="PythonStreamingDirectKafkaWordCount")
    #ssc = StreamingContext(sc, 2)


    #sc.setLogLevel("WARN")

    #rdd = sc.parallelize([1, 2, 3, 4])
    #col = rdd.map(lambda x : 2*x).collect()

    #for x in col :
    #    print x

    spark = SparkSession.builder \
        .master("local[*]") \
        .appName("Stage example") \
        .getOrCreate()

    #rdd = spark.sparkContext.parallelize([1, 2, 3, 4])
    #col = rdd.map(lambda x : 2*x).collect()

    #for x in col :
    #    print x
    df = spark \
        .readStream \
        .format("kafka") \
        .option("kafka.bootstrap.servers", "localhost:9092") \
        .option("subscribe", "iotmsgs") \
        .load()

# not needed:
#        .option("startingOffsets", "earliest") \
    #dfString = df.selectExpr("CAST(key AS STRING)", "CAST(value AS STRING)")
    dfString = df.selectExpr("CAST(key AS STRING)", "CAST(value AS STRING)")
    #dfString.

    # Define the schema for my high level JSON:

    #schema = StructType() \
    #    .add("type", StringType()) \
    #    .add("id", StringType()) \
    #    .add("ts", TimestampType()) \
    #    .add("payload", StringType())

    schema = StructType() \
        .add("type", StringType()) \
        .add("id", StringType()) \
        .add("ts", TimestampType()) \
        .add("payload", StructType().add("WindSpeed",DoubleType()).add("WindDirection",DoubleType())  )
        #.add("payload", StringType())

    dfJson = df.select( \
        col("key").cast("string"),
        from_json(col("value").cast("string"), schema).alias("data"))

    dfFlat = dfJson.select(col("data.*"))
    #events.select(from_json("a", schema).alias("c"))

    # window average:
    #dfCount = dfFlat.groupBy(window(col("ts"), "5 minutes"),col("id")).count()

    dfAgg = dfFlat.groupBy(window(col("ts"), "5 minutes"),col("id")).agg(avg("payload.WindSpeed"))

    #query = dfCount.writeStream \
    #query = dfString.writeStream \
    #query = dfFlat.writeStream \
    query = dfAgg.writeStream \
        .format("console") \
        .option("truncate", "false") \
        .outputMode("complete") \
        .start()

    query.awaitTermination()
            





