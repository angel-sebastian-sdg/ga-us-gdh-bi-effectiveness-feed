package com.gdh_bi_effectiveness_feed.us.test

import com.gdh_bi_effectiveness_feed.us.commons.UtilsBiEffectiveness
import org.scalatest._

class FeatureBasicTest extends FeatureSpec with GivenWhenThen with Matchers with BeforeAndAfterAll with BeforeAndAfterEach {

  var utils: Option[UtilsBiEffectiveness] = None: Option[UtilsBiEffectiveness]
  val NULL_VALUE:Null = null


  override def beforeEach() : Unit = {
    super.beforeEach()
    System.setProperty("hadoop.home.dir", "C:\\winutils")
    sys.props("testing") = "true"
    sys.props("nousehive") = "true"
    System.setProperty("spark.master", "local")
    utils = Option(new UtilsBiEffectiveness)
  }

  override def afterEach(): Unit = {
    super.afterEach()
    utils.get.spark.sparkContext.stop()
    utils = NULL_VALUE
  }

}
