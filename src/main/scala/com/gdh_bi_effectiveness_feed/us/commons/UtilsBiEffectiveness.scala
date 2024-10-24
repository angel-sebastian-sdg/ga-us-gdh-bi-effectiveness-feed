package com.gdh_bi_effectiveness_feed.us.commons

import com.beamsuntory.bgc.commons.{MyLogger, Utils}
import org.apache.hadoop.fs.Path
import org.apache.spark.sql.functions.col
import org.apache.spark.sql.{Column, DataFrame, SaveMode, SparkSession}

import java.io.File

class UtilsBiEffectiveness extends Utils {

  import spark.implicits._

  def envChecker(path: String): String ={
    sys.props.get("testing") match {
      case Some("true") => testResources
      case _ => storage.concat(env_short).concat(path)
    }
  }

  override val env_short: String = sys.props.get("testing") match {
    case Some("true") => "testing"
    case _ =>
      if (env.split(hyphen).head.equals("qa")) {
        "bstdl"
      } else if (env.split(hyphen).head.equals("dev")) {
        "dev-bigdata-storage"
      } else {
        env.split(hyphen).head
      }
  }

  val env_name = if (env_short == "qa") "bstdl"
  else if (env_short == "bspdl") "bspdl"
  else if (env_short == "bstdl") "bstdl"
  else if (env_short == "dev") "dev-bigdata-storage"
  else "test"

  val filePath = if (env_name == "bspdl") "gs://" + env_name + "-raw-us-bi-effectiveness-nav"
  else if (env_name == "bstdl") "gs://" + env_name + "-raw-us-bi-effectiveness-nav"
  else if (env_name == "test") "src/test/resources"
  else throw new Exception("ERROR: Filepath not identified. Exiting...")

  val src_sys_nm = "BI EFFECTIVENESS"

  val tableList = List(
//    ("GDH_SILVER.dim_account", "account_key"),
//    ("GDH_SILVER.dim_address", "address_key"),
//    ("GDH_SILVER.dim_product_additional", "product_key"),
//    ("GDH_SILVER.dim_product", "product_key"),
//    ("GDH_SILVER.dim_store_additional", "store_key"),
    ("GDH_SILVER.dim_store", "store_key")
  )

  val excelFormat = "com.crealytics.spark.excel"
  val csvFormat = "csv"

  val skuSheet = "'SKUs & Parameters'!A3:L9999"
  val skuPath = "/mapping_files/SGWSSalesNavSKUsOrig.xlsx"
  val kpiPath = "/mapping_files/KPIUniverse.xlsx"
  val tdlPath = "/mapping_files/TDLUpdates.xlsx"
  val salesNavPath = "/sales_nav"

  def getFilesToLoad: List[String] = {
    var files = List[String]()
    if (env_short == "qa" || env_short == "bspdl" || env_short == "bstdl" || env_short == "dev")  {
      val gcsBucket = new Path(filePath + salesNavPath)
      val conf = spark.sparkContext.hadoopConfiguration
      val listOfFiles = gcsBucket.getFileSystem(conf).listFiles(gcsBucket, true)
      while (listOfFiles.hasNext) {
        files = files :+ listOfFiles.next().getPath.toString
      }
      files
    } else {
      val file = new File(filePath + salesNavPath.replace("\\","\\\\"))
      files = file.listFiles.filter(_.isFile).filter(_.getName.endsWith(".csv")).map(_.getPath).toList
    }
    files
  }

  def readTableFromHive(table: String) : DataFrame = {
    var df:DataFrame = spark.emptyDataFrame
    if (env_short == "qa" || env_short == "bspdl" || env_short == "bstdl" || env_short == "dev") {
      df = spark.read.table(table)
    } else {
      df = spark.read
        .format(csvFormat)
        .option("header", "true")
        .option("delimiter", ",")
        .option("inferSchema", "true") // Infer data types
        .load(filePath + slash + table + dot + csvFormat)
    }
    df
  }

  def getExcelFilesToLoad(fileName: String) : DataFrame = {
    var df:DataFrame = spark.emptyDataFrame
    if (env_short == "qa" || env_short == "bspdl" || env_short == "bstdl" || env_short == "dev") {
     df = readBQTable(gdhBigQueryProject + fileName)
    } else {
      df = spark.read
            .format(csvFormat)
            .option("header", "true")
            .option("delimiter", ",")
            .option("inferSchema", "true") // Infer data types
            .load(filePath + slash + fileName + dot + csvFormat)
    }
    df
  }

  def getDataFromExcel(spark: SparkSession, inputPath: String, sheet: String = "Sheet1", hasHeader: String = "True"): DataFrame = {
    val readExcel = spark.read.format(excelFormat)
      .option("header", hasHeader)
      .option("dataAddress", sheet)
      .option("treatEmptyValuesAsNulls", "True")
      .option("inferSchema", "True")
      .load(inputPath)

    readExcel
  }

  def generateId(cols: Seq[String]): Column = {
    import org.apache.spark.sql.functions._

    val concatExpr = concat_ws(",", cols.map(c => concat(lit(s"$c="), col(c))): _*)
    md5(concatExpr)
  }

}


