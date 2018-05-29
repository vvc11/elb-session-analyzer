package entities

/**
  * Class which holds information provided in the user input file
  */
class LogEntry extends Serializable {
  var rawTimestamp: String = _
  var timestamp: Long = _
  var lbname: String = _
  var visitorIp: String = _
  var instanceIp: String = _
  var reqProcessingTime: Float = _
  var backendProcessingTime: Float = _
  var resProcessingTime: Float = _
  var elbStatusCode: Integer = _
  var backendStatusCode: Integer = _
  var receivedBytes: Integer = _
  var sentBytes: Integer = _
  var request: String = _
  var ua: String = _
  var requestType: String = _
  var page: String = _
  var rawPage: String = _
  override def toString: String = {
    "Timestamp : "+timestamp+" lbname : "+lbname+" visitorIp : "+visitorIp+" Raw page "+rawPage
  }
}
