"""
 Processes stream from kafka using spark structured streaming
 Usage: iotconsumer.py <broker_list> <msg_topic>

 To run this on your local machine, you need to setup Kafka and create a
   producer first, see:
 http://kafka.apache.org/documentation.html#quickstart

 and then run the example
    `$ spark-submit \
       --packages org.apache.spark:spark-sql-kafka-0-10_2.11:2.3.3 \
       iotconsumer.py \
       localhost:9092 iotmsgs`

    spark-submit \
       --packages org.apache.spark:spark-sql-kafka-0-10_2.11:2.3.3 \
       iotconsumer.py \
       localhost:9092 iotmsgs
"""
from __future__ import print_function

import sys
import re

#from pyspark import SparkContext
from pyspark.sql import SparkSession
#from pyspark.streaming import StreamingContext
from pyspark.streaming.kafka import KafkaUtils

#from operator import add

from pyspark.sql.types import *
from pyspark.sql.functions import *

if __name__ == "__main__":
    if len(sys.argv) != 3:
        print("Usage: iotconsumer.py <broker_list> <msg_topic>", file=sys.stderr)
        exit(-1)

    windowLength = "5 minutes"

    brokers, topic = sys.argv[1:]

    spark = SparkSession.builder \
        .appName("Kafka Structured Streaming processing of wind app") \
        .getOrCreate()

    #    .master("local[*]") \

    #spark.sparkContext.setLogLevel("ERROR")
    spark.sparkContext.setLogLevel("WARN")

    # Sensors have to be registered, for now there is a CSV with the info we need from them
    pwsInfoDF = spark.read \
        .format("csv") \
        .option("header","true") \
        .load("pwsInfo.csv")

    #pwsInfoDF.show()

    kafkaDF = spark \
        .readStream \
        .format("kafka") \
        .option("kafka.bootstrap.servers", brokers) \
        .option("subscribe", topic) \
        .load()

#        .option("kafka.bootstrap.servers", "localhost:9092") \


    # not needed, but possible to use:
    #    .option("startingOffsets", "earliest") \

    #################################################
    # Generic JSON Processing 
    #################################################

    # Select only the key and val columns, convert from UTF-8 back to string
    stringDF = kafkaDF.selectExpr(
        "CAST(key AS STRING) AS keyStr",
        "CAST(value AS STRING) AS valStr")

    # Define the genericSchema for the high level JSON (data is still a string):
    # complete genericSchema for reading:
    genericSchema = StructType() \
        .add("guid", StringType()) \
        .add("destination", StringType()) \
        .add("eventTime", TimestampType()) \
        .add("payload", StructType() \
            .add("format",StringType()) \
            .add("data",StringType()) \
        )

#            .add("data",StructType() \
#                .add("WindSpeed",DoubleType()) \
#                .add("WindDirection",DoubleType()) \
#            ) \

    # Filter only the json keys (only format suported anyway),
    # apply generic schema and make the structure flat again
    jsonDF = stringDF \
        .where(col("keyStr")=="json") \
        .select(from_json(col("valStr"), genericSchema).alias("data")) \
        .select(col("data.*"))

    #################################################
    # Personal Weather Station Processing 
    #################################################

    # Schema for the data portion of a PWS reading
    pwsReadingSchema = StructType() \
        .add("WindDirectionDegrees",DoubleType()) \
        .add("WindSpeedMPH",DoubleType()) \
        .add("WindSpeedGustMPH",DoubleType())

    # Apply the schema to "payload.data" only for pws:reading messages
    pwsReadingDF = jsonDF \
        .where(col("payload.format")=="urn:windchaser:pws:reading") \
        .withColumn("data",from_json(col("payload.data"), pwsReadingSchema)) \
        .drop("payload")

    # To keep format:
    #    .withColumn("format",col("payload.format")) \

    # Calculate the following window aggregations:
    #
    # avgWindSpeedMPH :
    #     Average of WindSpeedMPH in the time window
    #
    # avgWindDirDegrees :
    #     Average of WindSpeedDegrees based on decomposing angles into x and y
    #     component and averaging them and calculate back the angle, a simple
    #     average will give bad results when the wind direction fluctuates
    #     around north (some readings 0 some around 360).
    #
    # maxWindSpeedGustMPH :
    #     In case of the WindSpeedGustMPH we want to aggregate it as the
    #     maximum reading from the sensor during the window
    #
    pwsReadingStatsDF = pwsReadingDF \
        .withColumn("WindDirRads",radians("data.WindDirectionDegrees")) \
        .withColumn("WindDirX",cos("WindDirRads")) \
        .withColumn("WindDirY",sin("WindDirRads")) \
        .groupBy(window(col("eventTime"), windowLength),col("guid")) \
        .agg(
            avg("data.WindSpeedMPH").alias("avgWindSpeedMPH"),
            avg("data.WindDirectionDegrees").alias("avgDir"),
            ( (degrees(atan2(avg("WindDirY"),avg("WindDirX"))) + lit(360)) % lit(360)).alias("avgWindDirDegrees"),
            max("data.WindSpeedGustMPH").alias("maxWindSpeedGustMPH"),
            count(lit(1)).alias("cnt"))

    # Sink the stats to console, for now using complete but update with a
    # watermark could be used
    pwsReadingStatsQuery = pwsReadingStatsDF.writeStream \
        .format("console") \
        .option("truncate", "false") \
        .outputMode("complete") \
        .start()

    #################################################
    # Device Processing 
    #################################################

    # Processing of devices:
    deviceReadingSchema = StructType() \
        .add("lat",DoubleType()) \
        .add("lon",DoubleType())

    deviceReadingDF = jsonDF \
        .where(col("payload.format")=="urn:windchaser:device:reading") \
        .withColumn("data",from_json(col("payload.data"), deviceReadingSchema)) \
        .drop("payload")

    # Find all the devices around a sensor with a given radius (join 2 sets based on distance)
    nearByDevicesDF = deviceReadingDF.withColumnRenamed("guid","deviceGuid").join(
        pwsInfoDF,
        hypot(col("data.lat") - pwsInfoDF.lat, col("data.lon") - pwsInfoDF.lon ) <= lit(0.02),
        "inner")

    #test1DF = testDF.withColumn("math", pow(col("data.lat") - col("lat"),2.0) )
    #test1DF = testDF.withColumn("math",
    #        hypot(col("data.lat") - col("lat"), col("data.lon") - col("lon") ) )#<= lit(0.01)  )
    

    # Calculate in the given window how many different devices were close to the PWS
    # Note: countDistinct is not supported in streaming, the approx version is supported
    nearByDevicesWindowDF = nearByDevicesDF \
        .groupBy(window(col("eventTime"), windowLength),col("guid")) \
        .agg(approxCountDistinct("deviceGuid").alias("deviceCount"))

    nearByDevicesQuery = nearByDevicesWindowDF.writeStream \
        .format("console") \
        .option("truncate", "false") \
        .outputMode("complete") \
        .start()

    #nearByDevicesQuery.awaitTermination()

    # Wait for all streams
    spark.streams.awaitAnyTermination()

