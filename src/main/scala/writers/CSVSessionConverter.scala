package writers
import entities.Session

class CSVSessionConverter extends SessionWriter {
  override def writeSession(session: Session): String = {
    if (session == null)
      ""
    else
      session.visitor+","+session.rawStartTime+","+session.rawEndTime+","+session.pages.distinct.length
  }

  override def writeSchema: String = {
    "visitorIP,startTime,endTime,uniquePages"
  }
}
