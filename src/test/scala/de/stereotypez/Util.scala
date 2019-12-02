package de.stereotypez

import java.time.temporal.ChronoUnit
import org.dcm4che3.data.{Attributes, ElementDictionary}

object Util {

  def keywordOf(tag: Int): String = {
    ElementDictionary.getStandardElementDictionary.keywordOf(tag)
  }
  def yearsDiff(att: Attributes, tag1: Int, tag2: Int): Long = {
    ChronoUnit.YEARS.between(toLocalDateTime(att.getDate(tag1)), toLocalDateTime(att.getDate(tag2)))
  }
  def msDiff(att: Attributes, tag1: Int, tag2: Int): Long = {
    ChronoUnit.MILLIS.between(toLocalDateTime(att.getDate(tag1)), toLocalDateTime(att.getDate(tag2)))
  }

}
