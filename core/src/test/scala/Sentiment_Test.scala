import com.gudvin.tsa.Utils.NLPUtils
import org.scalatest.{BeforeAndAfter, FunSuite}

/**
  * Created by vinita on 7/15/16.
  */
class Sentiment_Test extends FunSuite with BeforeAndAfter{
var result: String = null
  before{
    result = ""
  }

  after{
    result = ""
  }

  test("testing sentiment function"){
    val message = "I'm not happy. you are happy."
    val pipeline = NLPUtils.getPipeline
    val result =  NLPUtils.detectSentiment(message, pipeline)
    assert(result==0)
  }
}
