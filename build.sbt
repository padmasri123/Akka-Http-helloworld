name := "helloworldakkahttp"

version := "0.1"

scalaVersion := "2.13.6"
val akkaVersion = "2.6.15"
val akkaHttpVersion = "10.2.4"

libraryDependencies ++= Seq(
  // akka streams
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  // akka http
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,

)
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"