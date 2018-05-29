import java.text.SimpleDateFormat
//import java.util.Date

import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.functions.{to_date,col,date_format}
//import org.apache.spark.sql.functions.{window,col,expr}
import org.apache.spark.sql.functions.desc
//import org.apache.spark.sql.types._
//import org.apache.spark.sql.SQLContext.impl

// Converting mac to unix:
// awk '{ gsub("\r", "\n"); print $0;}' businesses_plus.tsv > businesses_plus.csv

object Assignment4 extends App{
  println("Assignment 4")

  val spark = SparkSession.builder
    .master("local[*]")
    .appName("Spark Word Count")
    .getOrCreate()

  import spark.implicits._

  val hw4root = "/Users/roquealex/Documents/ucsc-ext/DBDA.X400.3/Assignment4"
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
    postal_code: Int,
    latitude: Double,
    longitude: Double
  )

  val businessSplitRDD = business.map(_.split("\t"))
  businessSplitRDD.persist()

  val cleanDouble = (x:String) => if(x.trim()=="") 0.0 else x.trim().toDouble
  val cleanZip = (str: String) => str.trim() match  {
    case x if (!x.matches("\\d+")) => -2
    case x if (x.length()==9) => x.substring(0,5).toInt
    case x if (x.length()==5) => x.toInt
    case _ => -1
  }
  val businessRDD = businessSplitRDD
    .map(p =>Business(
      p(0).trim.toInt,
      p(1),
      p(2),
      p(3),
      cleanZip(p(4)),
      cleanDouble(p(5)),
      cleanDouble(p(6))
      //if(p(3)=="N/A") None else Some(p(3)),
      //if (p.length<5) None else Some(p(4))
    ))//.filter(m => m.description=="EMPTY")
  businessRDD.collect().foreach(println)

  // A Few wrong zip codes:
  //"941102019" "941"       "941033148" "941"
  //"CA" "CA  94523" "0"
  // Many off the gird address  Off The Grid  contain odd postal codes


  // 3) Which 20 businesses got lowest scores?
  //(inspections_plus.csv, businesses_plus.csv)
  //
  //(This should be low score rather than lowest score)
  //
  //Expected columns - (business_id,name,address,city,postal_code,score)

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





}


