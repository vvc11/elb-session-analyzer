import java.io.File
import java.io.PrintWriter
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import parsers.ELBAccessLogParser
import session.TimeSessionSplitter
import writers.CSVSessionConverter

object ELBSessionAnalyzer {

  def main (args: Array[String]): Unit = {

    var inputTextFileLocation = null.asInstanceOf[String]
    var outputFileLocation = null.asInstanceOf[String]
    var sessionTimeout = 900000
    var summaryFile = "/Users/vishnuc/Downloads/ELBSessionsSummary"
    if (args.length.compareTo(4) == 0) {
      inputTextFileLocation = args(0)
      outputFileLocation = args(1)
      sessionTimeout = args(2).toInt
      summaryFile = args(3)
    } else {
      inputTextFileLocation = "/Users/vishnuc/Downloads/2015_07_22_mktplace_shop_web_log_sample.log"
      outputFileLocation = "/Users/vishnuc/Downloads/elbsessions.csv"
    }


    val conf = new SparkConf().setAppName("Paytm Weblog Challenge").setMaster("local[*]")
    val sc = new SparkContext(conf)
    val file = sc.textFile(inputTextFileLocation)
    val parser = new ELBAccessLogParser()
    val sessionSplitter = new TimeSessionSplitter(sessionTimeout)
    val logentryRdd = file.map( parser.readLineToLogEntry )
    //Group all logs by visitor IP and port
    val usergroupedData = logentryRdd.keyBy( _.visitorIp ).groupByKey()
    //Create sessions for these entries
    val userSessions = usergroupedData.map( entry => sessionSplitter.splitToSessions(entry._2, entry._1))
    //Explode the RDD, flattening it to sessions
    var res = userSessions.flatMap( _.toStream )
    val converter = new CSVSessionConverter()
    //Sorting sessions by duration
    res = res.sortBy( _.getDuration, ascending = false )
    //Save these sessions in output file
    val filewriter = new PrintWriter(new File(outputFileLocation))
    filewriter.append(converter.writeSchema() +"\n")
    res.collect().sortBy(entry => entry.getDuration ).map( converter.writeSession ).foreach(entry =>
      filewriter
      .append(entry+"\n"))
    filewriter.close()
    //When calculating average session, I'm including only those sessions where a user's activity lasted for more
    // than one page
    val activeSessions = res.filter(entry => entry.pages.length.compareTo(1) > 0)
    //As we need both the number of sessions and the total duration to calculate the average
    //I'm using collect action to gather these entries in a list, so that instead of calling 2 actions (count() and sum()),
    // we can get both the values with collect().
    val sessionLengths = activeSessions.map( _.getDuration ).collect()
    var totalsessionDuration = 0.0
    //Calculating cumulative session duration
    sessionLengths.foreach( totalsessionDuration += _ )
    val averageSessionLength = totalsessionDuration/sessionLengths.length

    val summary = "Average session duration : " + averageSessionLength + " Number of sessions " + sessionLengths.length+ " Total session duration " + totalsessionDuration
    //Printing summary just for debugging
    println(summary)
    val summaryWriter = new PrintWriter(new File(summaryFile))
    summaryWriter.write(summary)
    summaryWriter.close()
  }



}
