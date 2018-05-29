package writers

import entities.Session

trait SessionWriter extends Serializable{

  def writeSession( session : Session): String

  def writeSchema(): String

}
