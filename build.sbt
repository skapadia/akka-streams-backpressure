name := "AkkaStreams"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-slf4j" % "2.4.3",
  "com.typesafe.akka" %% "akka-stream" % "2.4.3",
  "com.typesafe.akka" %% "akka-stream-testkit" % "2.4.3" % "test",
  "com.typesafe.play" %% "play" % "2.5.0",
  "org.typelevel" %% "cats" % "0.4.1",
  "io.spray" %% "spray-can" % "1.3.2",
  "io.spray" %% "spray-json" % "1.3.+",
  "io.spray" %% "spray-routing-shapeless2" % "1.3.2",
  "io.spray" %% "spray-httpx" % "1.3.2",
  "io.spray" %% "spray-client" % "1.3.2"
)
    