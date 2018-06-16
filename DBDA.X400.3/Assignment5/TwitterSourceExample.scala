import org.apache.spark.sql.SparkSession
import org.structured_streaming_sources.twitter.TwitterStreamingSource
import org.apache.spark.sql.types.{DataType, TimestampType}
import org.apache.spark.sql.functions._
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.sql.expressions.UserDefinedFunction

import scala.collection.mutable


/**
  * Created by hluu on 3/11/18.
  */
object TwitterSourceExample {
  private val SOURCE_PROVIDER_CLASS = TwitterStreamingSource.getClass.getCanonicalName

  def main(args: Array[String]): Unit = {
    println("TwitterSourceExample")

    val providerClassName = SOURCE_PROVIDER_CLASS.substring(0, SOURCE_PROVIDER_CLASS.indexOf("$"))
    println(providerClassName)

    if (args.length != 4) {
      println("Usage: <consumer key>, <consumer secret> <access token> <access token secret>")
      sys.exit(1)
    }


    val Array(consumerKey, consumerSecret, accessToken, accessTokenSecret) = args.take(4)

    // create a Spark session
    val spark = SparkSession
      .builder
      .appName("TwitterStructuredStreaming")
      .master("local[*]")
      .getOrCreate()

    println("Spark version: " + spark.version)

    val stopWordsRDD = spark.sparkContext.textFile("src/main/resources/stop-words.txt")
    val posWordsRDD = spark.sparkContext.textFile("src/main/resources/pos-words.txt")
    val negWordsRDD = spark.sparkContext.textFile("src/main/resources/neg-words.txt")

    val positiveWords = posWordsRDD.collect().toSet
    val negativeWords = negWordsRDD.collect().toSet
    val stopWords = stopWordsRDD.collect().toSet

    val tweetDF = spark.readStream
                       .format(providerClassName)
                       .option(TwitterStreamingSource.CONSUMER_KEY, consumerKey)
                       .option(TwitterStreamingSource.CONSUMER_SECRET, consumerSecret)
                       .option(TwitterStreamingSource.ACCESS_TOKEN, accessToken)
                       .option(TwitterStreamingSource.ACCESS_TOKEN_SECRET, accessTokenSecret)
                         .load()

    tweetDF.printSchema()

    val folder = "src/main/resources"

    val positive = s"$folder/pos-words.txt"
    val negative = s"$folder/neg-words.txt"
    val stop = s"$folder/stop-words.txt"

    val stopDF = spark.read.format("csv")
      .option("header","false")
      .option("inferSchema","true")
      .load(stop)
      .select(lower(col("_c0")).as("word"))
    stopDF.persist()
    stopDF.show()
    stopDF.printSchema()
    /*
*/

    /*
    root
     |-- text: string (nullable = true)
     |-- user: string (nullable = true)
     |-- userLang: string (nullable = true)
     |-- createdDate: timestamp (nullable = true)
     |-- isRetweeted: boolean (nullable = true)
     */

    val enTweetDF = tweetDF
      .where(col("userLang")==="en")
      .where(col("isRetweeted")===false)

    val lowerTextDF = enTweetDF
      .withColumn("lowercase",lower(col("text")))

    val splitWordsDF = lowerTextDF
      .withColumn("word_list",split(col("lowercase"),"\\W+"))

    /*
    // Approach failed due to requiring multiple aggregations so moving towards using UDFs on the array of words:
    //Exception in thread "main" org.apache.spark.sql.AnalysisException: Multiple streaming aggregations are not supported with streaming DataFrames/Datasets;;
    val flatWordsDF = splitWordsDF
      .withColumn("word",explode(col("word_list")))
      .filter(col("word")=!="") // Ehrn the text ends in . there is typically an "" at the end
    // Remove stopwords:
    val wordsDF = flatWordsDF
      .join(
        stopDF,
        flatWordsDF.col("word")===stopDF.col("word"),
        "left_anti")
    */

    // WrappedArray required instead of Array according to exception
    def udfFilterFromSet(wordList: Broadcast[Set[String]]): UserDefinedFunction = {
      udf {(a: mutable.WrappedArray[String]) => {a.filter(!wordList.value(_))}}
    }

    def udfCountFromSet(wordList: Broadcast[Set[String]]): UserDefinedFunction = {
      udf {(a: mutable.WrappedArray[String]) => {a.count(wordList.value(_))}}
    }

    // Removing stop words and the empty "" word
    val noStopDF = splitWordsDF
      .withColumn(
        "filtered_word_list",
        udfFilterFromSet(spark.sparkContext.broadcast(stopWords+""))(col("word_list")))

    val countPosNegDF = noStopDF
      .withColumn("positive_count", udfCountFromSet(spark.sparkContext.broadcast(positiveWords))(col("filtered_word_list")))
      .withColumn("negative_count", udfCountFromSet(spark.sparkContext.broadcast(negativeWords))(col("filtered_word_list")))

    val classPosNegDF = countPosNegDF
      .withColumn("positive", col("positive_count") > col("negative_count"))
      .withColumn("negative", col("positive_count") < col("negative_count"))
      .withColumn("neutral", col("positive_count") === col("negative_count"))


    val win30sSummaryDF = classPosNegDF
      .withWatermark("createdDate","1 minute")
      .groupBy(window(col("createdDate"),"30 seconds"))
      .agg(
        sum(col("positive").cast("Int")).as("positive_total"),
        sum(col("negative").cast("Int")).as("negative_total"),
        sum(col("neutral").cast("Int")).as("neutral_total"),
        count(col("text")).as("total")
      )

    /*
    def udfCheckWord(wordList: Broadcast[Set[String]]): UserDefinedFunction = {
      udf {(s: String) => if (wordList.value(s)) 1 else 0 }
    }

    //val positiveWords = posWordsRDD.collect().toSet
    //val negativeWords = negWordsRDD.collect().toSet

    val wordsPosNegDF = wordsDF
      .withColumn("positive", udfCheckWord(spark.sparkContext.broadcast(positiveWords))(col("word")))
      .withColumn("negative", udfCheckWord(spark.sparkContext.broadcast(negativeWords))(col("word")))

    val wordsSummaryDF = wordsPosNegDF
      .withWatermark("createdDate","1 minute")
      .groupBy(col("text"), col("user"), col("createdDate"))
      .agg(
        sum("positive").as("positiveCount"),
        sum("negative").as("negativeCount"))
      .withColumn("score", col("positiveCount") - col("negativeCount"))

    val windowSummaryDF = wordsSummaryDF
      .groupBy(window(col("createdDate"),"30 seconds"))
      .agg(sum("score"))
      */

    // original:
    //val tweetQS = tweetDF.writeStream.format("console").option("truncate", false).start()

    //val tweetQS = wordsSummaryDF.writeStream.format("console").option("truncate", false).start()
    //val tweetQS = classPosNegDF.writeStream.format("console").option("truncate", false).start()
    val tweetQS = win30sSummaryDF.writeStream.format("console").option("truncate", false).start()

    //Exception in thread "main" org.apache.spark.sql.AnalysisException: Multiple streaming aggregations are not supported with streaming DataFrames/Datasets;;



    //Thread.sleep(1000 * 35)
    //Thread.sleep(1000 )

    tweetQS.awaitTermination()
  }
}
