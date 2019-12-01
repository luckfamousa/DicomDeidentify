package de.stereotypez

import spray.json.{JsString, JsValue, RootJsonFormat}
import spray.json.DefaultJsonProtocol._

object ActionCode {
  implicit object ActionCodeFormat extends RootJsonFormat[ActionCode] {
    override def read(json: JsValue): ActionCode = json.convertTo[String] match {
      case "X" => new `X`
      case "Z" => new `Z`
      case "C" => new `C`
      case "D" => new `D`
      case "K" => new `K`
      case "U" => new `U`
      case "X/Z/U*" => new `X/Z/U*`
      case "X/Z/D" => new `X/Z/D`
      case "X/Z" => new `X/Z`
      case "X/D" => new `X/D`
      case "Z/D" => new `Z/D`
    }
    override def write(obj: ActionCode): JsValue = JsString(obj.code)
  }
}
class ActionCode(val code: String)
class PrimitiveActionCode(code: String) extends ActionCode(code)
class CompoundActionCode(code: String) extends ActionCode(code)

class `X` extends PrimitiveActionCode("X")
class `Z` extends PrimitiveActionCode("Z")
class `C` extends PrimitiveActionCode("C")
class `D` extends PrimitiveActionCode("D")
class `K` extends PrimitiveActionCode("K")
class `U` extends PrimitiveActionCode("U")

class `X/Z/U*` extends CompoundActionCode("X/Z/U*")
class `X/Z/D` extends CompoundActionCode("X/Z/D")
class `X/Z` extends CompoundActionCode("X/Z")
class `X/D` extends CompoundActionCode("X/D")
class `Z/D` extends CompoundActionCode("Z/D")

