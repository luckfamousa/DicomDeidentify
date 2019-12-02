package de.stereotypez

import ActionCode._

case class ConfidentialityProfileAttribute (
   attributeName: String,
   tag: String,
   retiredFromPS36: Boolean,
   inStdCompIODFromPS33: Boolean,
   basicProfile: ActionCode,
   retainSafePrivateOption: Option[ActionCode],
   retainUIDsOption: Option[ActionCode],
   retainDeviceIdentOption: Option[ActionCode],
   retainInstIdentOption: Option[ActionCode],
   retainPatientCharsOption: Option[ActionCode],
   retainLongFullDatesOption: Option[ActionCode],
   retainLongModifDatesOption: Option[ActionCode],
   cleanDescOption: Option[ActionCode],
   cleanStructContOption: Option[ActionCode],
   cleanGraphOption: Option[ActionCode]
)