package de.stereotypez

import spray.json.{JsString, JsValue, RootJsonFormat}
import spray.json.DefaultJsonProtocol._

object ActionCode extends Enumeration {
  type ActionCode = Value
  val `X`, `Z`, `C`, `D`, `K`, `U`, `X/Z/U*`, `X/Z/D`, `X/Z`, `X/D`, `Z/D` = Value
  
  def of(s: String): ActionCode = s match {
    case "X"      => `X`
    case "Z"      => `Z`
    case "C"      => `C`
    case "D"      => `D`
    case "K"      => `K`
    case "U"      => `U`
    case "X/Z/U*" => `X/Z/U*`
    case "X/Z/D"  => `X/Z/D`
    case "X/Z"    => `X/Z`
    case "X/D"    => `X/D`
    case "Z/D"    => `Z/D`
    case c        => throw new UnknownActionCodeException(s"Code '$c' is unknown.") 
  }

  override def toString(): String = this match {
      case `X`      => "X"
      case `Z`      => "Z"
      case `C`      => "C"
      case `D`      => "D"
      case `K`      => "K"
      case `U`      => "U"
      case `X/Z/U*` => "X/Z/U*"
      case `X/Z/D`  => "X/Z/D"
      case `X/Z`    => "X/Z"
      case `X/D`    => "X/D"
      case `Z/D`    => "Z/D"
      case c        => throw new UnknownActionCodeException(s"Code '$c' is unknown.") // should never happen
  }

  implicit object ActionCodeFormat extends RootJsonFormat[ActionCode] {
    override def read(json: JsValue): ActionCode = ActionCode.of(json.convertTo[String])
    override def write(obj: ActionCode): JsValue = JsString(obj.toString)
  }
}

class UnknownActionCodeException(msg: String) extends RuntimeException(msg)

