package session

import entities.LogEntry
import entities.Session

trait SessionSplitter {

  def splitToSessions(entries: Iterable[LogEntry], visitor: String): Iterable[Session]

}
