import org.apache.spark.sql.SparkSession
//import org.apache.spark.sql.functions.{window,col,expr}
//import org.apache.spark.sql.types._

// Converting mac to unix:
// awk '{ gsub("\r", "\n"); print $0;}' businesses_plus.tsv > businesses_plus.csv

object Assignment4 extends App{
  println("Assignment 4")

  val spark = SparkSession.builder
    .master("local[*]")
    .appName("Spark Word Count")
    .getOrCreate()

  val hw4root = "/Users/roquealex/Documents/ucsc-ext/DBDA.X400.3/Assignment4"
  val baseDir = s"$hw4root/sf-food-tsv"

  // Homework should start from here
  //val baseDir = "/FileStore/tables/sf-food-tsv"
  val raw_inspections = spark.sparkContext.textFile(s"$baseDir/inspections_plus.tsv")
  val violations = spark.sparkContext.textFile(s"$baseDir/violations_plus.tsv")
  val business = spark.sparkContext.textFile(s"$baseDir/businesses_plus.txt")

  //raw_inspections.take(10).foreach(println)
  //violations.take(10).foreach(println)
  business.take(10).foreach(println)

}



