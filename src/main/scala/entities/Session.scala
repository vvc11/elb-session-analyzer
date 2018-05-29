package entities


/**
  * Every activity provided in the access logs will be mapped to a session and depending on the activity, one or more
  * than one entry in the access log will be a part of Session object
  * @param visitorIp The unique string which identifies a user
  */
class Session(visitorIp: String) extends Serializable {

  var visitor     : String       = visitorIp
  var pages       : List[String] = List()
  var startTime   : Long         = _
  var endTime     : Long         = _
  var fullPages   : List[String] = _
  var rawStartTime: String       = _
  var rawEndTime  : String       = _

  override def toString: String = {
    "Visitor : "+visitor+" number of pages "+pages.length+"which are "+ pages +" duration "+(endTime - startTime)/1000
  }

  def getDuration: Long = {
    endTime - startTime
  }
}
