import java.text.SimpleDateFormat
//import java.util.Date

import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.functions.{to_date,col,date_format,avg,round,udf,count,sum,expr,min}
//import org.apache.spark.sql.functions.{window,col,expr}
import org.apache.spark.sql.functions.desc
//import org.apache.spark.sql.types._
//import org.apache.spark.sql.SQLContext.impl

// Converting mac to unix:
// awk '{ gsub("\r", "\n"); print $0;}' businesses_plus.tsv > businesses_plus.csv

object Assignment4 extends App{
  println("Assignment 4")

  val sparkConf = SparkSession.builder
    .master("local[*]")
    .appName("Spark Word Count")
    .config("spark.driver.bindAddress", "127.0.0.1")
    .config("spark.driver.host", "127.0.0.1")
    //.config("spark.local.ip", "127.0.0.1")
    //.config("spark.ui.enabled", "false")


  val spark = sparkConf.getOrCreate()
  //.setExecutorEnv("")
  //spark.conf.set("spark.driver.bindAddress", "127.0.0.1")

  import spark.implicits._

  val hw4root = "/Users/roque/Documents/sandbox/ucsc-ext/DBDA.X400.3/Assignment4"
  val baseDir = s"$hw4root/sf-food-tsv"

  // Homework should start from here
  //val baseDir = "/FileStore/tables/sf-food-tsv"
  val raw_inspections = spark.sparkContext.textFile(s"$baseDir/inspections_plus.tsv")
  val violations = spark.sparkContext.textFile(s"$baseDir/violations_plus.tsv")
  val business = spark.sparkContext.textFile(s"$baseDir/businesses_plus.tsv")

  //raw_inspections.take(10).foreach(println)
  //violations.take(10).foreach(println)
  //business.take(10).foreach(println)

  val inspectionsSplitRDD = raw_inspections.map(_.split("\t"))
  inspectionsSplitRDD.persist()

  //val dateFormat = new SimpleDateFormat("yyyyMMdd")
  // Allowing NA/ null type is reserved word
  case class Inspection(business_id:Int, score:Option[Int], date:String, Type:String)

  val inspectionsRDD = inspectionsSplitRDD
    .map(p =>Inspection(
      p(0).trim.toInt,
      if (p(1).trim == "") None else Some(p(1).trim.toInt),
      p(2),
      p(3)
    ))
  inspectionsRDD.take(10).foreach(println)

  val inspectionsDF = inspectionsRDD.toDF()
  inspectionsDF.show()
  inspectionsDF.printSchema()

  case class InspectionNoNA(business_id:Int, score:Int, date:String, Type:String)

  val inspectionsNoNaRDD = inspectionsSplitRDD
    .filter(_(1).trim()!="")
    .map(p =>InspectionNoNA(
      p(0).trim.toInt,
      p(1).trim.toInt,
      p(2),
      p(3)
    ))

  val inspectionsNoNaDF = inspectionsNoNaRDD.toDF()
  inspectionsNoNaDF.show()
  inspectionsNoNaDF.printSchema()


  // Question 1:
  //1) What is the inspection score distribution like? (inspections_plus.csv)
  //Expected output - (score, count) - order by score in descending order
  val query1 = inspectionsDF.na.drop().groupBy("score").count().orderBy(desc("score"))
  query1.show(100)

  val query1NoNA = inspectionsNoNaDF.groupBy("score").count().orderBy(desc("score"))
  query1NoNA.show(100)

    //.filter(m => m.length == 3)
    //.map(p => Movie(p(0), p(1), p(2).trim.toInt))

  //2) What is the risk category distribution like? (violations_plus.csv)
  //Expected output - (risk category, count)
  /*
  // Inferred schema:
  |-- business_id: integer (nullable = true)
  |-- date: integer (nullable = true)
  |-- violationTypeID: integer (nullable = true)
  |-- risk_category: string (nullable = true)
  |-- description: string (nullable = true)
  */
  val violationsSplitRDD = violations.map(_.split("\t"))
  violationsSplitRDD.persist() // There are 31 NA

  case class Violation(
                        business_id: Int,
                        date: String,
                        violationTypeID: Int,
                        risk_category: Option[String],
                        description: Option[String] )

  val violationsRDD = violationsSplitRDD
    .map(p =>Violation(
      p(0).trim.toInt,
      p(1),
      p(2).trim.toInt,
      if(p(3)=="N/A") None else Some(p(3)),
      if (p.length<5) None else Some(p(4))
    ))//.filter(m => m.description=="EMPTY")
  violationsRDD.collect().foreach(println)

  val violationsDF = violationsRDD.toDF()
  violationsDF.show()
  violationsDF.printSchema()

  // risk_category and description can be null
  val query2 = violationsDF.na.drop(Seq("risk_category")) .groupBy("risk_category").count()//.orderBy(desc("risk_category"))
  query2.show(100)

  /*
  golden
  +-------------+-----+
  |risk_category|count|
  +-------------+-----+
  |     Low Risk|24717|
  |Moderate Risk|15713|
  |          N/A|   31|
  |    High Risk| 6446|
  +-------------+-----+
  */

  // Reading business
  // Suggested schema:
  /*
  root
   |-- business_id: integer (nullable = true) *
   |-- name: string (nullable = true)  *
   |-- address: string (nullable = true) *
   |-- city: string (nullable = true) *
   |-- postal_code: string (nullable = true) *
   |-- latitude: double (nullable = true) *
   |-- longitude: double (nullable = true) *
   |-- phone_number: long (nullable = true)
   |-- tax_code: string (nullable = true)
   |-- business_certificate: integer (nullable = true)
   |-- application_date: string (nullable = true)
   |-- owner_name: string (nullable = true)
   |-- owner_address: string (nullable = true)
   |-- owner_city: string (nullable = true)
   |-- owner_state: string (nullable = true)
   |-- owner_zip: string (nullable = true)
   */

  case class Business(
    business_id: Int,
    name: String,
    address: String,
    city: String,
    postal_code: Option[Int],
    latitude: Option[Double],
    longitude: Option[Double]
  )

  val businessSplitRDD = business.map(_.split("\t"))
  businessSplitRDD.persist()

  // If lat and long are empty then assign null
  val cleanDouble = (x:String) => if(x.trim()=="") None else Some(x.trim().toDouble)

  // Assign null to no numeric or anything that is not 9 or 5 digit zip, 9 digit zip gets trimmed to 5
  val cleanZip = (str: String) => str.trim() match  {
    case x if (!x.matches("\\d+")) => None
    case x if (x.length()==9) => Some(x.substring(0,5).toInt)
    case x if (x.length()==5) => Some(x.toInt)
    case _ => None
  }

  // Wrong zip city:
  // FOSTER CITY|      94536| maybe union city
  // OAKLAND|      94501| Really alameda

  // There are many variations (and typos) for San Francisco name
  val cleanCity = (str: String) => str match {
    case "Sand Francisco" => "San Francisco"
    case "S F" => "San Francisco"
    case s if (s.matches("\"?[Ss]\\.?[Ff]\\.?.*")) => "San Francisco"
    case s if (s.toLowerCase().matches("\"?san fran.*")) => "San Francisco"
    case "SO. SAN FRANCISCO" => "So. San Francisco"
    case "SO.S.F." => "So. San Francisco"
    case _ => str
  }

  val businessRDD = businessSplitRDD
    .map(p =>Business(
      p(0).trim.toInt,
      p(1),
      p(2),
      cleanCity(p(3)), // City
      //p(3), // City
      cleanZip(p(4)),
      cleanDouble(p(5)),
      cleanDouble(p(6))
      //if(p(3)=="N/A") None else Some(p(3)),
      //if (p.length<5) None else Some(p(4))
    ))//.filter(m => m.description=="EMPTY")
  //businessRDD.collect().foreach(println)

  val businessDF = businessRDD.toDF()
  businessDF.show()
  businessDF.printSchema()

  // A Few wrong zip codes:
  //"941102019" "941"       "941033148" "941"
  //"CA" "CA  94523" "0"
  // Many off the gird address  Off The Grid  contain odd postal codes

  // There is an NA in the names 82649	N/A	2250 Irving St	SF


  // 3) Which 20 businesses got lowest scores?
  //(inspections_plus.csv, businesses_plus.csv)
  //
  //(This should be low score rather than lowest score)
  //
  //Expected columns - (business_id,name,address,city,postal_code,score)


  val lowestScoresDF = inspectionsDF.na.drop()
    .groupBy("business_id")
    .agg(min("score").as("lowest_score"))
    //.orderBy("lowest_score","business_id")
    //.limit(20)
  //lowestScoresDF.show(25)

  // My businessDF has already this fields only: business_id,name,address,city,postal_code,latitude,longitude
  val businessLowestScoresDF = businessDF
    .join(lowestScoresDF,Seq("business_id"),"inner")
    .orderBy("lowest_score","business_id")
    //.limit(20)

  businessLowestScoresDF.show()

  businessDF
    .groupBy("city")
    .count()
    .show()
  businessDF
    //.where($"city" !== "San Francisco")
    //.where($"city" !== "So. San Francisco")
    //.where($"city" !== "")
    .where($"city" === "CA")
    .show(

  // 4) Which 20 businesses got highest scores?
  //(inspections_plus.csv, businesses_plus.csv)
  //
  //Expected columns - (business_id,name,address,city,postal_code,score)

  // 5) Among all the restaurants that got 100 score, what kind of violations did they get (if any)
  //(inspections_plus.csv, violations_plus.csv)
  //
  //(Examine "High Risk" violation only)
  //
  //Expected columns - (business_id, risk_category, date, description)
  //
  //Note - format the date in (month/day/year)

  // Version A

  // Get the business that got 100
  /*
  val business_id_100 = inspectionsDF.where($"score"===100)
  business_id_100.persist()

  val query5A = violationsDF
    .join(business_id_100,Seq("business_id"),"left_semi")
    .where($"risk_category"==="High Risk")
    .withColumnRenamed("date","dateString")
    .withColumn("newDate",to_date(col("dateString"),"yyyyMMdd"))
    .select(
      col("business_id"),
      col("risk_category"),
      date_format(col("newDate"),"M/d/y").as("date"),
      col("description"))

  query5A.orderBy("business_id","date").show(20, false)

  val query5B = violationsDF
    .join(business_id_100,Seq("business_id","date"),"left_semi")
    .where($"risk_category"==="High Risk")
    .withColumnRenamed("date","dateString")
    .withColumn("newDate",to_date(col("dateString"),"yyyyMMdd"))
    .select(
      col("business_id"),
      col("risk_category"),
      date_format(col("newDate"),"M/d/y").as("date"),
      col("description"))

  query5B.orderBy("business_id","date").show(20, false)
  */

  // 6) Average inspection score by zip code
  //(inspections_plus.csv, businesses_plus.csv)
  //
  //Expected columns - (zip, average score with only two digits after decimal) - order by average inspection score in descending order

  /*
   |-- business_id: integer (nullable = false)
 |-- name: string (nullable = true)
 |-- address: string (nullable = true)
 |-- city: string (nullable = true)
 |-- postal_code: integer (nullable = true)
 |-- latitude: double (nullable = true)
 |-- longitude: double (nullable = true)
   */

  //businessDF = businessRDD.toDF()

  /*
  val zips = businessDF.select("business_id","postal_code").na.drop()
  //zips.show()
  val joinScoreZip = inspectionsDF.na.drop()
    .select("business_id","score")
    .join(
      zips,
      Seq("business_id"),
      "inner")
  //joinScoreZip.show()

  val query6 = joinScoreZip
    .groupBy("postal_code")
    .agg(round(avg("score"),2).as("average"))
    .orderBy(desc("average"))

  query6.show(50)



  // 7) Compute the proportion of all businesses in each neighborhood that have incurred at least one of the violations
  //"High risk vermin infestation"
  //"Moderate risk vermin infestation"
  //"Sewage or wastewater contamination”
  //"Improper food labeling or menu misrepresentation"
  //"Contaminated or adulterated food”
  //"Reservice of previously served foods"
  //"Expected output: zip code, percentage"
  //This question is asking for each neighborhood, what is the proportion of businesses that have incurred at least one of the above nasty violations
  //
  //Note: use UDF to determine which violations match with one of the above extremely bad violations
  //
  //Expected columns - (zip code, total violation count, extreme violation count, proportion with only two digits after decimal)
  //
  //Order the result by the proportion in descending order

  // null case works fine:
  def isNastyViolation(str : String): Boolean  =  str match {
    case "High risk vermin infestation" => true
    case "Moderate risk vermin infestation" => true
    case "Sewage or wastewater contamination" => true
    case "Improper food labeling or menu misrepresentation" => true
    case "Contaminated or adulterated food" => true
    case "Reservice of previously served foods" => true
    //case null => true
    case _ => false
  }

  //val myBool: Boolean = true
  violationsDF.printSchema()
  val isNastyViolationUDF = udf(isNastyViolation(_:String))
  //violationsDF.na.drop(Seq("risk_category")) .groupBy("risk_category").count()//.orderBy(desc("risk_category"))
  // 54 has misxture of tru and false
  // 10 is only false
  // 3855 is only false has null, 11 elements
  //println(violationsDF.na.drop(Seq("risk_category")).count())

  // Create a new column classifying if the violation belongs to the bad group.
  // the risk category N/A gets discarded, empty (null) description is still used
  val violationClassifiedDF = violationsDF.na.drop(Seq("risk_category"))
    .withColumn("is_nasty_violation",isNastyViolationUDF(col("description")))

  // Summarize showing number of violations and the number of bad violations per business
  // Create a boolean representing that business had any of the bad ones
  val violationSummaryDF = violationClassifiedDF
    .groupBy("business_id")
    .agg(
      count("is_nasty_violation").as("violation_count"),
      sum(col("is_nasty_violation").cast("Int")).as("extreme_violation_count"))
    .withColumn("has_extreme_violation",col("extreme_violation_count")>0)

   //   .orderBy("business_id")

    //.where("is_nasty_violation")
  //println(violationSummary.count())
  //violationSummaryDF.show()

  // merge with the zip data frame that has all the business ids and zip codes
  val businessViolationSummaryDF = zips
    .join(violationSummaryDF,Seq("business_id"),"left_outer")
  //businessViolationSummaryDF.orderBy("business_id").show()

  // Clean NA
  val businessViolationSummaryNoNaDF = businessViolationSummaryDF.na.fill(Map(
    "violation_count" -> 0,
    "extreme_violation_count" -> 0,
    "has_extreme_violation" -> false
  ))
  //businessViolationSummaryNoNaDF.orderBy("business_id").show()
  //println(businessViolationSummaryNoNaDF.count())

  val violationsPerZipDF = businessViolationSummaryNoNaDF
    .groupBy("postal_code")
    .agg(
      sum("violation_count").as("total_violation_count"),
      sum("extreme_violation_count").as("total_extreme_violation_count"),
      round(
        sum(col("has_extreme_violation").cast("Int"))/count("has_extreme_violation"),
        2).as("proportion_of_business"))
    .orderBy(desc("proportion_of_business"))
  violationsPerZipDF.show()
  println(violationsPerZipDF.count)
  */

}


