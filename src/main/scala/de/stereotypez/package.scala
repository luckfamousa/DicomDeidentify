package de

import java.time.{LocalDate, LocalDateTime, ZoneId}
import java.util.Date

import de.stereotypez.ActionCode.ActionCodeFormat
import org.slf4j.LoggerFactory

import scala.io.{BufferedSource, Source}
import spray.json.DefaultJsonProtocol._
import spray.json._

package object stereotypez {

  private val LOG = LoggerFactory.getLogger(classOf[Deidentify])

  implicit def toLocalDate(dateToConvert: Date): LocalDate = dateToConvert.toInstant.atZone(ZoneId.systemDefault).toLocalDate
  implicit def toDate(dateToConvert: LocalDate) : Date = java.util.Date.from(dateToConvert.atStartOfDay.atZone(ZoneId.systemDefault).toInstant)
  implicit def toLocalDateTime(dateToConvert: Date): LocalDateTime = LocalDateTime.ofInstant(dateToConvert.toInstant, ZoneId.systemDefault)
  implicit def toDate(dateToConvert: LocalDateTime) : Date = Date.from(dateToConvert.atZone(ZoneId.systemDefault).toInstant)

  def loadConfidentialityProfileAttributes(): Seq[Deidentifier] = {

    implicit val format: RootJsonFormat[ConfidentialityProfileAttribute] = jsonFormat(ConfidentialityProfileAttribute.apply,
"Attribute Name",
"Tag",
"Retd. (from PS3.6)",
"In Std. Comp. IOD (from PS3.3)",
"Basic Prof.",
"Rtn. Safe Priv. Opt.",
"Rtn. UIDs Opt.",
"Rtn. Dev. Id. Opt.",
"Rtn. Inst. Id. Opt.",
"Rtn. Pat. Chars. Opt.",
"Rtn. Long. Full Dates Opt.",
"Rtn. Long. Modif. Dates Opt.",
"Clean Desc. Opt.",
"Clean Struct. Cont. Opt.",
"Clean Graph. Opt."
    )

    // modified from https://github.com/neurosnap/dicom_codify/blob/master/json/deidentify.json
    val src = Source.fromInputStream(getClass.getResourceAsStream("/deidentify.json"))
    val jsnStr = try src.mkString finally src.close()

    val jsn = jsnStr.parseJson
    val tagFmt = """\(\s*([\p{XDigit}x]{4})\s*,\s*([\p{XDigit}x]{4})\s*\)""".r

    jsn.convertTo[List[ConfidentialityProfileAttribute]] map { d =>
      d.tag match {
        case _ if d.tag.contains("odd")          => OddGroupDeidentifier(d)
        case tagFmt(g, e) if d.tag.contains("x") => RangeDeidentifier(g, e, d)
        case tagFmt(g, e)                        => TagDeidentifier(g, e, d)
      }
    }
  }

  lazy val deidentifiers: Seq[Deidentifier] = loadConfidentialityProfileAttributes()
}
