name := """dse-scala-example"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

scalacOptions ++= Seq("-unchecked", "-feature", "-Ybreak-cycles")

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  "com.datastax.cassandra" % "cassandra-driver-core" % "3.1.4",
  "com.datastax.cassandra" % "cassandra-driver-mapping" % "3.1.4",
  "com.datastax.cassandra" % "dse-driver" % "1.1.2",
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test
)

