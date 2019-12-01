package de.stereotypez

trait ProfileOption {
  def apply(d: Deidentifier): Option[ActionCode]
}

object BasicProfile extends ProfileOption {
  def apply(d: Deidentifier) = Some(d.cpa.basicProfile)
}
object RetainLongModifDatesOption extends ProfileOption {
  def apply(d: Deidentifier) = d.cpa.retainLongModifDatesOption
}
object RetainLongFullDatesOption extends ProfileOption {
  def apply(d: Deidentifier) = d.cpa.retainLongFullDatesOption
}
object RetainUIDsOption extends ProfileOption {
  def apply(d: Deidentifier) = d.cpa.retainUIDsOption
}
object CleanGraphOption extends ProfileOption {
  def apply(d: Deidentifier) = d.cpa.cleanGraphOption
}
object RetainPatientCharsOption extends ProfileOption {
  def apply(d: Deidentifier) = d.cpa.retainPatientCharsOption
}
object RetainSafePrivateOption extends ProfileOption {
  def apply(d: Deidentifier) = d.cpa.retainSafePrivateOption
}
object CleanDescOption extends ProfileOption {
  def apply(d: Deidentifier) = d.cpa.cleanDescOption
}
object RetainDeviceIdentOption extends ProfileOption {
  def apply(d: Deidentifier) = d.cpa.retainDeviceIdentOption
}
object CleanStructContOption extends ProfileOption {
  def apply(d: Deidentifier) = d.cpa.cleanStructContOption
}
