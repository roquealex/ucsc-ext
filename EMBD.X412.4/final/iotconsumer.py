
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

    spark = SparkSession.builder \
        .master("local[*]") \
        .appName("Stage example") \
        .getOrCreate()

    #spark.sparkContext.setLogLevel("ERROR")
    spark.sparkContext.setLogLevel("WARN")

    # Sensors have to be registered, for now there is a CSV with the info we need from them
    pwsInfoDF = spark.read \
        .format("csv") \
        .option("header","true") \
        .load("pwsInfo.csv")

    #pwsInfoDF.show()
    #sys.exit(0);

    #rdd = spark.sparkContext.parallelize([1, 2, 3, 4])
    #col = rdd.map(lambda x : 2*x).collect()

    #for x in col :
    #    print x
    kafkaDF = spark \
        .readStream \
        .format("kafka") \
        .option("kafka.bootstrap.servers", "localhost:9092") \
        .option("subscribe", "iotmsgs") \
        .load()

# not needed:
#        .option("startingOffsets", "earliest") \
    #dfString = kafkaDF.selectExpr("CAST(key AS STRING)", "CAST(value AS STRING)")
    #dfString = kafkaDF.selectExpr("CAST(key AS STRING)", "CAST(value AS STRING)")


    # Select only the key and val columns, convert from UTF-8 back to string
    stringDF = kafkaDF.selectExpr(
        "CAST(key AS STRING) AS keyStr",
        "CAST(value AS STRING) AS valStr")

    # Define the genericSchema for my high level JSON (data is still a string):
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

    pwsReadingSchema = StructType() \
        .add("WindDirectionDegrees",DoubleType()) \
        .add("WindSpeedMPH",DoubleType()) \
        .add("WindSpeedGustMPH",DoubleType())

    pwsReadingDF = jsonDF \
        .where(col("payload.format")=="urn:windchaser:pws:reading") \
        .withColumn("data",from_json(col("payload.data"), pwsReadingSchema)) \
        .drop("payload")

    windowLength = "5 minutes"

    # To keep:
    #    .withColumn("format",col("payload.format")) \

    #flatDF = jsonDF.select(col("data.*"))
    #events.select(from_json("a", genericSchema).alias("c"))

    # window average:
    #dfCount = flatDF.groupBy(window(col("eventTime"), "5 minutes"),col("guid")).count()

    #>aggDF = flatDF.groupBy(window(col("eventTime"), "5 minutes"),col("guid")).agg(avg("payload.data.WindSpeed"),count(lit(1)))

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
    aggDF = pwsReadingDF \
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

    query = aggDF.writeStream \
        .format("console") \
        .option("truncate", "false") \
        .outputMode("complete") \
        .start()

    # Processing of devices:
    deviceReadingSchema = StructType() \
        .add("lat",DoubleType()) \
        .add("lon",DoubleType())

    deviceReadingDF = jsonDF \
        .where(col("payload.format")=="urn:windchaser:device:reading") \
        .withColumn("data",from_json(col("payload.data"), deviceReadingSchema)) \
        .drop("payload")

    # Original really bad:
    #testDF = deviceReadingDF.join(pwsInfoDF, (col("data.lat") - pwsInfoDF.lat) < lit(1.0), "inner")

    # Find all the devices around a sensor with a given radious (join based on distance)
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
        .agg(approxCountDistinct("deviceGuid"))

    #query = dfCount.writeStream \
    #query = dfString.writeStream \
    #query = pwsReadingDF.writeStream \
    #query = deviceReadingDF.writeStream \
    #query = nearByDF.writeStream \
    #query = aggDF.writeStream \

    nearByDevicesQuery = nearByDevicesWindowDF.writeStream \
        .format("console") \
        .option("truncate", "false") \
        .outputMode("complete") \
        .start()

        #.outputMode("complete") \
    #nearByDevicesQuery.awaitTermination()

    # Wait for all streams
    spark.streams.awaitAnyTermination()
            





