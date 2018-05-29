package parsers

import entities.LogEntry
import java.text.SimpleDateFormat
import scala.util.matching.Regex

class ELBAccessLogParser extends Serializable {

  /**
    * Logic where every line is split into tokens which will be mapped to a LogEntry logic
    * @param line Access log line that should be mapped to a logEntry
    * @return
    */
  protected def parserLine(line: String): Array[String] = {

    val token = line.split("\"")
    val firsthalf = token(0).split(" ")
    val request = token(1)
    val ua = token(3)

    firsthalf ++ Array(request, ua)

  }

  def readLineToLogEntry(line: String): LogEntry = {
    readToLogEntry(parserLine(line))
  }

  val pattern: Regex = "https?://paytm\\.com:?\\d*/?".r

  val pathpattern: Regex = "(\\d+|\\.com)(/\\w+)+".r

  protected def readToLogEntry(array: Array[String]): LogEntry = {
    val logEntry = new LogEntry()
    val dateParser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    logEntry.rawTimestamp = array(0)
    logEntry.timestamp = dateParser.parse(array(0).substring(0,19)).getTime
    logEntry.lbname = array(1)
    logEntry.visitorIp = array(2)
    logEntry.instanceIp = array(3)
    logEntry.reqProcessingTime = array(4).toFloat
    logEntry.backendProcessingTime = array(5).toFloat
    logEntry.resProcessingTime = array(6).toFloat
    logEntry.elbStatusCode = array(7).toInt
    logEntry.backendStatusCode = array(8).toInt
    logEntry.receivedBytes = array(9).toInt
    logEntry.sentBytes = array(10).toInt
    logEntry.request = array(11)
    logEntry.ua = array(12)
    val requestToken = logEntry.request.split(" ")
    logEntry.requestType = requestToken(0)
    logEntry.rawPage = requestToken(1)

    val path = pathpattern.findAllIn(requestToken(1))
    var compstring = ""
    path.foreach( token => compstring += token )
    val pathtokens = compstring.split("/")
    //I'm assuming that http://paytm.com/shop/watch-adidas-2000 and http://paytm.com/shop/shoe-running-nike-XF500 are
    // don't count as 2 different pages (since they're both related to shopping) and http://paytm.com/paypi ,
    // http://paytm.com/offers are different websites.
    try {
      logEntry.page = pathtokens.toList(1)
    }
    catch {
      //This would only happen in the case where no path variable is present in the url. Eg https://paytm.com:443/ ,
      // https://paytm.com:443/?a=value1&b=value2. We map such examples to an empty string
      case e: Exception => logEntry.page = ""
    }
    logEntry
  }

}
