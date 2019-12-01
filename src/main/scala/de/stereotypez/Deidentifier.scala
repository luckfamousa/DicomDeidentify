package de.stereotypez

abstract class Deidentifier(val cpa: ConfidentialityProfileAttribute) {
  def matches(tag: Int): Boolean
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