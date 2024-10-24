package com.gdh_bi_effectiveness_feed.us.commons

import org.apache.spark.sql.Column
import org.apache.spark.sql.functions._


abstract class HarmonizationArrayCol {

  val selectHarColumns: Seq[Column] = Seq(
    col("Account Flagged Accounts").alias("account_flagged_accounts"),
    col("Account Customer Name").alias("account_customer_name"),
    col("Account TDLinx Code").alias("account_tdlinx_code"),
    col("Account SGWS Site").alias("account_sgws_site"),
    col("Account SGWS No").alias("account_sgws_no"),
    col("Account Address").alias("account_address"),
    col("Account City").alias("account_city"),
    col("Account State").alias("account_state"),
    col("Account Zip").alias("account_zip"),
    col("Account Premise").alias("account_premise"),
    col("Account Channel").alias("account_channel"),
    col("Account Sub-Channel").alias("account_sub_channel"),
    col("Account Owner").alias("account_owner"),
    col("Account Banner").alias("account_banner"),
    col("Product Universe 9L % Chg").cast("double").alias("product_universe_9l_perc_chg"),
    col("Product Universe $ % Chg").cast("double").alias("product_universe_dollar_perc_chg"),
    col("Product Universe 9L Index").cast("double").alias("product_universe_9l_index"),
    col("Product Universe $ Index").cast("double").alias("product_universe_dollar_index"),
    col("Product Universe Pr Index").cast("double").alias("product_universe_pr_index"),
    col("Product Universe %ACV$").cast("double").alias("product_universe_perc_acv_dollar"),
    col("Product Universe CoDist").alias("product_universe_codist"),
    col("sku").alias("sku"),
    col("dollar").cast("double").alias("dollar"),
    col("dollar % Chg").cast("double").alias("dollar_perc_chg"),
    col("dollar BDI").cast("double").alias("dollar_bdi"),
    col("dollar Chg").cast("double").alias("dollar_chg"),
    col("dollar SPPD").cast("double").alias("dollar_sppd"),
    col("9L ").cast("double").alias("nine_l"),
    col("9L % Chg").cast("double").alias("nine_l_perc_chg"),
    col("9L BDI").cast("double").alias("nine_l_bdi"),
    col("9L Chg").cast("double").alias("nine_l_chg"),
    col("9L SPPD").cast("double").alias("nine_l_sppd"),
    col("SGL").alias("sgl")
  )

  val selectTDLUpdateColumns: Seq[Column] = Seq(
    column("Account").alias("account"),
    column("City").alias("city"),
    column("State").alias("state"),
    column("Current Tag").alias("current_tag"),
    column("New Tag").alias("new_tag")
  )

}
