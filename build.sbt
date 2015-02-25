val generateCollections = taskKey[Unit]("Generates backlinks from GloBI interaction data into EOL traits.")

lazy val root = (project in file(".")).
  settings(
    name := "backwarden",
    version := "1.0",
    scalaVersion := "2.11.4"
  )

resolvers ++= Seq(
  "anormcypher" at "http://repo.anormcypher.org/",
  "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases/",
  "Sonatype repo" at Resolver.sonatypeRepo("public").root,
  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
)

libraryDependencies ++= Seq(
  "org.scalatest" % "scalatest_2.11" % "2.2.1" % "test",
  "org.anormcypher" %% "anormcypher" % "0.6.0",
  "com.github.tototoshi" %% "scala-csv" % "1.2.0"
)

mainClass in assembly := Some("org.eol.globi.traits.TraitExtractorCLI")





