name := """RestToGraphQL"""
organization := "hegworks"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.5"

libraryDependencies += guice

libraryDependencies ++= Seq(
	// Default Dependencies:
	"org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test,

	//Dependencies to enable reactive mongo:
    // Enable reactive mongo for Play 2.8
    // "org.reactivemongo" %% "play2-reactivemongo" % "1.0.4-play28",


/*     // Provide JSON serialization for reactive mongo  
    "org.reactivemongo" %% "reactivemongo-play-json-compat" % "1.0.1-play28",
    // Provide BSON serialization for reactive mongo  
    "org.reactivemongo" %% "reactivemongo-bson-compat" % "0.20.13",
    // Provide JSON serialization for Joda-Time
    "com.typesafe.play" %% "play-json-joda" % "2.7.4", */
)

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "hegworks.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "hegworks.binders._"
