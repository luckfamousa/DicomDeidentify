package de.stereotypez

abstract class Deidentifier(val cpa: ConfidentialityProfileAttribute) {
  def matches(tag: Int): Boolean
  /*
  def basicProfile(): ActionCode = ActionCode(cpa.basicProfile)
  def retainLongModifDatesOption(): ActionCode = ActionCode(cpa.retainLongModifDatesOption.getOrElse(basicProfile))
  def retainLongFullDatesOption(): ActionCode = ActionCode(cpa.retainLongFullDatesOption.getOrElse(basicProfile))
  def retainUIDsOption(): ActionCode = ActionCode(cpa.retainUIDsOption.getOrElse(basicProfile))
  def cleanGraphOption(): ActionCode = ActionCode(cpa.cleanGraphOption.getOrElse(basicProfile))
  def retainPatientCharsOption(): ActionCode = ActionCode(cpa.retainPatientCharsOption.getOrElse(basicProfile))
  def retainSafePrivateOption(): ActionCode = ActionCode(cpa.retainSafePrivateOption.getOrElse(basicProfile))
  def cleanDescOption(): ActionCode = ActionCode(cpa.cleanDescOption.getOrElse(basicProfile))
  def retainDeviceIdentOption(): ActionCode = ActionCode(cpa.retainDeviceIdentOption.getOrElse(basicProfile))
  def cleanStructContOption(): ActionCode = ActionCode(cpa.cleanStructContOption.getOrElse(basicProfile))
  */
}

object TagDeidentifier {
  def apply(group: String, element: String, cpa: ConfidentialityProfileAttribute) = new TagDeidentifier(group, element, cpa)
}
class TagDeidentifier(group: String, element: String, cpa: ConfidentialityProfileAttribute) extends Deidentifier(cpa)  {
  private val _tag: BigInt = BigInt(group+element, 16)
  override def matches(tag: Int): Boolean = _tag == tag
}

object RangeDeidentifier {
  def apply(group: String, element: String, cpa: ConfidentialityProfileAttribute) = new RangeDeidentifier(group, element, cpa)
}
class RangeDeidentifier(group: String, element: String, cpa: ConfidentialityProfileAttribute) extends Deidentifier(cpa)  {
  private val _from: BigInt = BigInt((group+element).replaceAll("x","0"), 16)
  private val _to: BigInt = BigInt((group+element).replaceAll("x","F"), 16)
  override def matches(tag: Int): Boolean = tag >= _from && tag <= _to
}

object OddGroupDeidentifier {
  def apply(cpa: ConfidentialityProfileAttribute) = new OddGroupDeidentifier(cpa)
}
class OddGroupDeidentifier(cpa: ConfidentialityProfileAttribute) extends Deidentifier(cpa)  {
  override def matches(tag: Int): Boolean = (tag >> 16)  % 2 == 1
}