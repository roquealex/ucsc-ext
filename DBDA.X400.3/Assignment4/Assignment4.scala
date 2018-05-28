import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.rdd.RDD
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

  // Allowing NA/ null
  case class Inspection(business_id:Int, score:Option[Int], date:String, Type:String)

  val inspectionsRDD = inspectionsSplitRDD
    .map(p =>Inspection(
      p(0).trim.toInt,
      if (p(1).trim == "") None else Some(p(1).trim.toInt),
      p(2),
      p(3)
    ))
  //inspectionsRDD.take(10).foreach(println)

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

}



