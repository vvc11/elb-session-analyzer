package session

import entities.LogEntry
import entities.Session

class TimeSessionSplitter(userThreshold: Int) extends Serializable with SessionSplitter {

  var sessionBreakThreshold: Int = userThreshold

  /**
    * This is the default value (15 minutes) if no threshold is specified
    */
  def this() {
    this(900000)
  }

  override def splitToSessions(logEntries: Iterable[LogEntry], visitor: String): List[Session] = {
    if (logEntries == null)
      return List[Session]()
    var prevEntry = null.asInstanceOf[LogEntry]
    var sessions = List[Session]()
    var session = new Session(visitor)
    for (logEntry <- logEntries) {
      if (prevEntry == null) {
        session.startTime = logEntry.timestamp
        session.endTime = logEntry.timestamp
        session.rawStartTime = logEntry.rawTimestamp
        session.rawEndTime = logEntry.rawTimestamp
        if (logEntry.page != null) session.pages = logEntry.page :: session.pages
        else session.pages = List[String]()
      }
      else if (logEntry.timestamp - prevEntry.timestamp > 900000) {
        session.endTime = prevEntry.timestamp
        session.rawEndTime = prevEntry.rawTimestamp
        sessions = session :: sessions
        session = new Session(visitor)
        session.startTime = logEntry.timestamp
        session.rawStartTime = logEntry.rawTimestamp
        session.endTime = logEntry.timestamp
        session.rawEndTime = logEntry.rawTimestamp
        if (logEntry.page != null) session.pages = List[String](logEntry.page)
        else session.pages = List[String]()
      }
      else {
        if (logEntry.page != null) session.pages = logEntry.page :: session.pages
        session.endTime = prevEntry.timestamp
        session.rawEndTime = prevEntry.rawTimestamp
      }
      prevEntry = logEntry
    }
    sessions = session :: sessions
    sessions.reverse
  }

}
