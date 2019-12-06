package de.stereotypez

import java.io.File

case class Config(
  in: File = new File("."),
  out: File = new File("."),
  overwrite: Boolean = false,
  studyInstanceUID: String = "",
  verbose: Boolean = false)