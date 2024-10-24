package com.gdh_bi_effectiveness_feed.us.load

import com.beamsuntory.bgc.commons.DataFrameUtils._
import com.gdh_bi_effectiveness_feed.us.commons.{HarmonizationArrayCol, UtilsBiEffectiveness}
import org.apache.log4j.Logger
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}
import org.apache.spark.sql.functions.{array, col, concat, explode, lit, max, monotonically_increasing_id, split, struct, sum, trim, when}

class Harmonization(utils: UtilsBiEffectiveness) extends HarmonizationArrayCol {

  val logger: Logger = Logger.getLogger(getClass.getName)

  def populate(): Unit = {
    logger.info("Data Harmonization Started...")
  }

}
