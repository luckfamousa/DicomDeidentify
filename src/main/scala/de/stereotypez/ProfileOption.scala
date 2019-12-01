package de.stereotypez

import  ActionCode._

trait ProfileOption {
  def apply(d: Deidentifier): Option[ActionCode]
}

object BasicProfile extends ProfileOption {
  def apply(d: Deidentifier): Option[ActionCode] = Some(d.cpa.basicProfile)
}
object RetainLongModifDatesOption extends ProfileOption {
  def apply(d: Deidentifier): Option[ActionCode] = d.cpa.retainLongModifDatesOption
}
object RetainLongFullDatesOption extends ProfileOption {
  def apply(d: Deidentifier): Option[ActionCode] = d.cpa.retainLongFullDatesOption
}
object RetainUIDsOption extends ProfileOption {
  def apply(d: Deidentifier): Option[ActionCode] = d.cpa.retainUIDsOption
}
object CleanGraphOption extends ProfileOption {
  def apply(d: Deidentifier): Option[ActionCode] = d.cpa.cleanGraphOption
}
object RetainPatientCharsOption extends ProfileOption {
  def apply(d: Deidentifier): Option[ActionCode] = d.cpa.retainPatientCharsOption
}
object RetainSafePrivateOption extends ProfileOption {
  def apply(d: Deidentifier): Option[ActionCode] = d.cpa.retainSafePrivateOption
}
object CleanDescOption extends ProfileOption {
  def apply(d: Deidentifier): Option[ActionCode] = d.cpa.cleanDescOption
}
object RetainDeviceIdentOption extends ProfileOption {
  def apply(d: Deidentifier): Option[ActionCode] = d.cpa.retainDeviceIdentOption
}
object CleanStructContOption extends ProfileOption {
  def apply(d: Deidentifier): Option[ActionCode] = d.cpa.cleanStructContOption
}
