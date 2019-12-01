package de.stereotypez

import ActionCode._

case class ConfidentialityProfileAttribute (
  retainLongModifDatesOption: Option[ActionCode],
  retainLongFullDatesOption: Option[ActionCode],
  basicProfile: ActionCode,
  retainUIDsOption: Option[ActionCode],
  cleanGraphOption: Option[ActionCode],
  retainPatientCharsOption: Option[ActionCode],
  attributeName: String,
  tag: String,
  retainSafePrivateOption: Option[ActionCode],
  inStdCompIODFromPS33: Boolean,
  cleanDescOption: Option[ActionCode],
  retiredFromPS36: Boolean,
  retainDeviceIdentOption: Option[ActionCode],
  cleanStructContOption: Option[ActionCode]
)
