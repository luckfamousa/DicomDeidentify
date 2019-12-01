package de.stereotypez

import org.dcm4che3.data.{Tag, VR}

/**
 * @author ${user.name}
 */
object App {
  
  def foo(x : Array[String]) = x.foldLeft("")((a,b) => a + b)
  
  def main(args : Array[String]) {

    val d = Deidentify()
      .keep(Tag.PatientSex, Tag.PatientSize, Tag.PatientWeight)
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
      // if not specified BasicProfile will be used
      .withOptions(RetainDeviceIdentOption, RetainLongModifDatesOption)


  }

}
