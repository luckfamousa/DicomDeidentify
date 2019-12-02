package de.stereotypez

import java.io.File
import java.nio.file.Files

import org.junit._
import Assert._
import org.dcm4che3.data.{Attributes, Tag, VR}
import org.dcm4che3.io.{DicomInputStream}


@Test
class Tests {

  def loadTestFile(name: String): (Attributes, Attributes) = {
    val din = new DicomInputStream(getClass.getResourceAsStream(s"/$name.dcm"))
    val fmi = din.readFileMetaInformation()
    val att = din.readDataset(-1, -1)
    (fmi, att)
  }


  @Test
  def test01() = {

    val name01 = "test01"
    val (fmi, att) = loadTestFile(name01)
    println(s"Running DICOM File '$name01.dcm' with TransferSytaxUID=${fmi.getString(Tag.TransferSyntaxUID)}")

    val d = Deidentify()
      .keep(
        Tag.PatientSex, Tag.PatientSize, Tag.PatientWeight,
        Tag.StudyDescription, Tag.SeriesDescription, Tag.ProtocolName)
      .withDummies("removed", Map(Tag.PatientName -> "JOHN^DOE"))
      .addSpecialHandler((att, tag, dsf, tsf) => {
        // this special handler intercepts all Tags with  temporal VR and
        // applies date shifting on their values
        // I always use this as DICOM Supp. 142 Table E.1-1 seems to be incomplete
        // (e.g. 'Instance Creation Date' is missing)
        if (att.getVR(tag).isTemporalType) {
          if (VR.TM == att.getVR(tag))
            att.setDate(tag, att.getVR(tag), tsf(att.getDate(tag)))
          else
            att.setDate(tag, att.getVR(tag), dsf(att.getDate(tag)))
          true
        }
        else false
      })
      .addSpecialHandler((att, tag, dsf, tsf) => {
        // hash PatientID
        // note that PatientID is of VR 'LO' which can hold 64 characters maximum
        // so using SHA-256 should be ok
        def sha256(s: String): String = {
          import java.nio.charset.StandardCharsets
          import java.security.MessageDigest
          MessageDigest.getInstance("SHA-256")
            .digest(s.getBytes(StandardCharsets.UTF_8))
            .map("%02X" format _).mkString
        }
        // add more Tags if you like
        tag match {
          case Tag.PatientID =>
            att.setString(tag, att.getVR(tag), sha256(att.getString(tag)))
            true
          case _ => false
        }
      })
      .addSpecialHandler((att, tag, dsf, tsf) => {

        // (mis-)use handler to remove all inline binary where
        // "burned-in annotations" are likely
        tag match {
          case Tag.BurnedInAnnotation if att.getString(tag,"NO").toUpperCase == "YES" =>
            att.tags
              .filter(t => att.getVR(t).isInlineBinary)
              .foreach(t => att.remove(tag))
            true

          case Tag.Modality if att.getString(tag,"").toUpperCase == "SC" =>
            att.tags
              .filter(t => att.getVR(t).isInlineBinary)
              .foreach(t => att.remove(tag))
            true

          case _ => false
        }
      })
      // if not specified BasicProfile will be used
      .withOptions(RetainDeviceIdentOption, RetainLongModifDatesOption)

    // collect tags to test
    val patID = att.getString(Tag.PatientID)
    val studDt = att.getDate(Tag.StudyDate)
    val studTm = att.getDate(Tag.StudyTime)
    val instTm = att.getDate(Tag.InstanceCreationTime)
    val calcAgePat = Util.yearsDiff(att, Tag.PatientBirthDate, Tag.StudyDate)
    val instCreaT = Util.msDiff(att, Tag.StudyTime, Tag.InstanceCreationTime)

    // run de-identification
    printSummary(att)
    d.execute(att)
    printSummary(att)

    // test
    assertEquals("JOHN^DOE", att.getString(Tag.PatientName))
    assertNotEquals(patID, att.getString(Tag.PatientID))
    assertNotEquals(studDt, att.getDate(Tag.StudyDate))
    assertNotEquals(studTm, att.getDate(Tag.StudyTime))
    assertNotEquals(instTm, att.getDate(Tag.InstanceCreationTime))
    assertEquals(Util.yearsDiff(att, Tag.PatientBirthDate, Tag.StudyDate), calcAgePat)
    assertEquals(Util.msDiff(att, Tag.StudyTime, Tag.InstanceCreationTime), instCreaT)
  }

  def printSummary(att: Attributes) = {

    println("-----------------------------------")
    println(s"PatientName: ${att.getString(Tag.PatientName)}")
    println(s"PatientID: ${att.getString(Tag.PatientID)}")
    println(s"PatientBirthDate: ${att.getString(Tag.PatientBirthDate)}")
    println(s"PatientAddress: ${att.getString(Tag.PatientAddress)}")
    println(s"PregnancyStatus: ${att.getString(Tag.PregnancyStatus)}")

    println(s"InstitutionName: ${att.getString(Tag.InstitutionName)}")
    println(s"InstitutionalDepartmentName: ${att.getString(Tag.InstitutionalDepartmentName)}")
    println(s"InstitutionAddress: ${att.getString(Tag.InstitutionAddress)}")
    println(s"ReferringPhysicianName: ${att.getString(Tag.ReferringPhysicianName)}")
    println(s"StationName: ${att.getString(Tag.StationName)}")

    println(s"StudyDescription: ${att.getString(Tag.StudyDescription)}")
    println(s"RequestedProcedureDescription: ${att.getString(Tag.RequestedProcedureDescription)}")
    println(s"PerformedProcedureStepDescription: ${att.getString(Tag.PerformedProcedureStepDescription)}")
    println(s"SeriesDescription: ${att.getString(Tag.SeriesDescription)}")
    println(s"ProtocolName: ${att.getString(Tag.ProtocolName)}")
    println(s"DeviceSerialNumber: ${att.getString(Tag.DeviceSerialNumber)}")

    println(s"StudyDate: ${att.getString(Tag.StudyDate)}")
    println(s"StudyTime: ${att.getString(Tag.StudyTime)}")
    println(s"InstanceCreationDate: ${att.getString(Tag.InstanceCreationDate)}")
    println(s"InstanceCreationTime: ${att.getString(Tag.InstanceCreationTime)}")

    println(s"Calculated age of Patient at StudyTime: ${Util.yearsDiff(att, Tag.PatientBirthDate, Tag.StudyDate)}Y")
    println(s"Instance created at relative StudyTime: ${Util.msDiff(att, Tag.StudyTime, Tag.InstanceCreationTime)}ms")

    /*
    att.tags foreach {
      case tag => println(s"${Util.keywordOf(tag)}: ${att.getString(tag)}")
      case _ => ()
    }
    */
  }

}