package code.snippet

import scala.xml.{Group, NodeSeq}
import net.liftweb.http.js._
import net.liftweb.http.js.JE.{Str, JsRaw}
import net.liftweb.http.SHtml
import net.liftweb.http.SHtml._
import net.liftweb.util.Helpers._
import net.liftweb.http.js.JsCmds.Alert
import net.liftweb.http.js.JsCmds._

import net.liftweb.http._
import S._
import SHtml._
import js._
import js.jquery._
import net.liftweb.http
import http.jquery._
import JqJsCmds._
import JsCmds._
import net.liftweb.common._
import util._
import net.liftweb.util.Helpers
import Helpers._
import _root_.scala.xml.{Text, NodeSeq}
import org.slf4j.LoggerFactory

class AjaxTimeoutSnippet {

  // local state for the counter
  var cnt = 0

  val logger = LoggerFactory.getLogger(getClass)

  // get the id of some elements to update
  val spanName: String = S.attr("id_name") openOr "cnt_id"
  val msgName: String = S.attr("id_msgs") openOr "messages"

  // build up an ajax <a> tag to increment the counter
  def doClicker(text: NodeSeq) =
    a(() => {
      cnt = cnt + 1;
      logger.info("clicked");
      SetHtml(spanName, Text( cnt.toString))
    }, text)

  // create an ajax select box
  def doSelect(msg: NodeSeq) =
    ajaxSelect((1 to 50).toList.map(i => (i.toString, i.toString)),
      Full(1.toString),
      v => DisplayMessage(msgName,
        bind("sel", msg, "number" -> Text(v)),
        5 seconds, 1 second))

  // build up an ajax text box
  def doText(msg: NodeSeq) =
    ajaxText("", v => DisplayMessage(msgName,
      bind("text", msg, "value" -> Text(v)),
      4 seconds, 1 second))


  def sample(xhtml: NodeSeq): NodeSeq = {
    logger.info("Called AjaxTimeoutSnippet.sample()")
    // bind the view to the functionality
    bind("ajax", xhtml,
      "clicker" -> doClicker _,
      "select" -> doSelect _,
      "text" -> doText _)
  }

  private def buildQuery(current: String, limit: Int): Seq[String] = {
    logger.info("Checking on server side with "+current+" limit "+limit)
    (1 to limit).map(n => current+""+n)
  }

  def time = Text(now.toString)

  def buttonClick = {
    import js.JE._

    "* [onclick]" #> SHtml.ajaxCall(ValById("the_input"),
      s => SetHtml("messages",
        <i>Text box is {s}</i>))
  }

  def showConfig(xhtml: NodeSeq): NodeSeq = {
    val currentTime = now.toString
    <span>Time of loading: {currentTime} . Ajax function timeout setting: {LiftRules.unusedFunctionsLifeTime.toString}</span>
  }

}
