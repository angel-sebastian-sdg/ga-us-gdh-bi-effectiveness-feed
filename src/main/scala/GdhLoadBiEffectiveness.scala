
import com.gdh_bi_effectiveness_feed.us.commons.UtilsBiEffectiveness
import com.gdh_bi_effectiveness_feed.us.load.Harmonization
import org.apache.log4j.{Level, Logger}

import scala.language.implicitConversions


object GdhLoadBiEffectiveness {

  val logger: Logger = Logger.getLogger(getClass.getName)
  val utils = new UtilsBiEffectiveness

  def main(args: Array[String]) {

    //Suppress Spark output
    Logger.getLogger("akka").setLevel(Level.ERROR)
    args match {
      case Array("-harmonization", _*) =>
        new Harmonization(utils).populate()
      case _ =>
        logger.error("ERROR: Invalid option.")
        throw new IllegalArgumentException("Wrong arguments. Usage: \n" +
          "GdhLoadBiEffectiveness -all | -harmonization | -account | -address | -product_additional | -product | -store_additional | -store | -sales_navigator_daily | -validation")
    }
  }

}