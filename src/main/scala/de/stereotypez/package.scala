package de

import java.time.{LocalDate, LocalDateTime, ZoneId}
import java.util.Date

import de.stereotypez.ActionCode.ActionCodeFormat

import scala.io.{BufferedSource, Source}
import spray.json.DefaultJsonProtocol._
import spray.json._

package object stereotypez {

  implicit def toLocalDate(dateToConvert: Date): LocalDate = dateToConvert.toInstant.atZone(ZoneId.systemDefault).toLocalDate
  implicit def toDate(dateToConvert: LocalDate) : Date = java.util.Date.from(dateToConvert.atStartOfDay.atZone(ZoneId.systemDefault).toInstant)
  implicit def toLocalDateTime(dateToConvert: Date): LocalDateTime = LocalDateTime.ofInstant(dateToConvert.toInstant, ZoneId.systemDefault)
  implicit def toDate(dateToConvert: LocalDateTime) : Date = Date.from(dateToConvert.atZone(ZoneId.systemDefault).toInstant)

  def loadConfidentialityProfileAttributes(): Seq[Deidentifier] = {

    implicit val format = jsonFormat(ConfidentialityProfileAttribute.apply,
      "Retain Long. Modif. Dates Option",
      "Retain Long. Full Dates Option",
      "Basic Profile",
      "Retain UIDs Option",
      "Clean Graph. Option",
      "Retain Patient Chars. Option",
      "Attribute Name",
      "Tag",
      "Retain Safe Private Option",
      "In Std. Comp. IOD (from PS3.3)",
      "Clean Desc. Option",
      "Retired (from PS3.6)",
      "Retain Device Ident. Option",
      "Clean Struct. Cont. Option"
    )

    // modified from https://github.com/neurosnap/dicom_codify/blob/master/json/deidentify.json
    val src = Source.fromInputStream(getClass.getResourceAsStream("/deidentify.json"))
    val jsnStr = try src.mkString finally src.close()
    val jsn = jsnStr.parseJson
    val tagFmt = """\(\s*([\p{XDigit}x]{4})\s*,\s*([\p{XDigit}x]{4})\s*\)""".r

    jsn.convertTo[List[ConfidentialityProfileAttribute]] map { d =>
      d.tag match {
        case "(odd,xxxx)"                        => OddGroupDeidentifier(d)
        case tagFmt(g, e) if d.tag.contains("x") => RangeDeidentifier(g, e, d)
        case tagFmt(g, e)                        => TagDeidentifier(g, e, d)
      }
    }
  }

  lazy val deidentifiers: Seq[Deidentifier] = loadConfidentialityProfileAttributes()
}
