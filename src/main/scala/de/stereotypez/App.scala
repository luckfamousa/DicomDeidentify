package de.stereotypez

import scopt.OParser

object App extends App {

  val builder = OParser.builder[Config]

  val parser = {
    import builder._
    OParser.sequence(
      programName("deidentify"),
      head("scopt", "4.x"),
      // option -f, --foo
      opt[Int]('f', "foo")
        .action((x, c) => c.copy(foo = x))
        .text("foo is an integer property"),
      // more options here...
    )

    // OParser.parse returns Option[Config]
    OParser.parse(parser, args, Config()) match {
      case Some(config) =>
      // do something
      case _ =>
      // arguments are bad, error message will have been displayed
    }
  }
}
