//import entities.LogEntry
//import session.TimeSessionSplitter
//
//object TestObject {
//
//  def main (args: Array[String]): Unit = {
//    val splitter = new TimeSessionSplitter()
//    val logEntry0 = new LogEntry()
//    val logEntry1 = new LogEntry()
//    val logEntry2 = new LogEntry()
//    val logEntry3 = new LogEntry()
//    val logEntry4 = new LogEntry()
//
//    logEntry0.page = "shop"
//    logEntry1.page = "shop"
//    logEntry2.page = "shop"
//    logEntry3.page = "a"
//    logEntry4.page = "garbage"
//
//    logEntry1.timestamp = 1000000
//    logEntry1.timestamp = 2000000
//    logEntry2.timestamp = 2200000
//    logEntry3.timestamp = 2300000
//    logEntry4.timestamp = 9000000
//
//    val result = splitter.splitToSessions(Array(logEntry0, logEntry1, logEntry2, logEntry3, logEntry4), "sdasd")
//    result.foreach( println )
//  }
//
//}
