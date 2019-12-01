package de.stereotypez

trait ProfileOption {
  def apply(d: Deidentifier): Option[ActionCode]
}

object BasicProfile extends ProfileOption {
  def apply(d: Deidentifier) = Some(ActionCode(d.cpa.basicProfile))
}
object RetainLongModifDatesOption extends ProfileOption {
  def apply(d: Deidentifier) = d.cpa.retainLongModifDatesOption.map(s => ActionCode(s))
}
object RetainLongFullDatesOption extends ProfileOption {
  def apply(d: Deidentifier) = d.cpa.retainLongFullDatesOption.map(s => ActionCode(s))
}
object RetainUIDsOption extends ProfileOption {
  def apply(d: Deidentifier) = d.cpa.retainUIDsOption.map(s => ActionCode(s))
}
object CleanGraphOption extends ProfileOption {
  def apply(d: Deidentifier) = d.cpa.cleanGraphOption.map(s => ActionCode(s))
}
object RetainPatientCharsOption extends ProfileOption {
  def apply(d: Deidentifier) = d.cpa.retainPatientCharsOption.map(s => ActionCode(s))
}
object RetainSafePrivateOption extends ProfileOption {
  def apply(d: Deidentifier) = d.cpa.retainSafePrivateOption.map(s => ActionCode(s))
}
object CleanDescOption extends ProfileOption {
  def apply(d: Deidentifier) = d.cpa.cleanDescOption.map(s => ActionCode(s))
}
object RetainDeviceIdentOption extends ProfileOption {
  def apply(d: Deidentifier) = d.cpa.retainDeviceIdentOption.map(s => ActionCode(s))
}
object CleanStructContOption extends ProfileOption {
  def apply(d: Deidentifier) = d.cpa.cleanStructContOption.map(s => ActionCode(s))
}
