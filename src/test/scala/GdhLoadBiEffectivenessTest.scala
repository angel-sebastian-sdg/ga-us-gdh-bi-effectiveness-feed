import com.gdh_bi_effectiveness_feed.us.commons.UtilsSgws
import org.apache.log4j.Logger
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.col
import org.junit.{Assert, Test}


class GdhLoadBiEffectivenessTest {
  sys.props("testing") = "true"
  val logger: Logger = Logger.getLogger(getClass.getName)

  SparkSession.builder().master("local").appName("spark session").getOrCreate()

  val utils = new UtilsSgws
  val spark: SparkSession = utils.spark
  val tab = "\t"

  @Test
  def biEffectivenessTest(): Unit = {

  }
}
