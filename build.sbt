
lazy val root = (project in file(".")).settings(
  name := "akka-ddd-sample"
).aggregate(api, domain, db, flyway)

import com.typesafe.sbt.SbtScalariform.ScalariformKeys

import scalariform.formatter.preferences._

val formatPreferences = FormattingPreferences()
  .setPreference(RewriteArrowSymbols, false)
  .setPreference(AlignParameters, true)
  .setPreference(AlignSingleLineCaseStatements, true)
  .setPreference(SpacesAroundMultiImports, true)
  .setPreference(DoubleIndentClassDeclaration, true)
  .setPreference(AlignArguments, true)

val commonSettings = Seq(
  version := "1.0.0"
  , scalaVersion := "2.11.8"
  , scalacOptions ++= Seq(
    "-feature"
    , "-deprecation"
    , "-unchecked"
    , "-encoding", "UTF-8"
    , "-language:existentials"
    , "-language:implicitConversions"
    , "-language:postfixOps"
    , "-language:higherKinds"
  )
  , libraryDependencies ++= Seq(
    "org.scalatest" %% "scalatest" % "3.0.0" % "test"
    , "org.slf4j" % "slf4j-api" % "1.7.21"
    , "ch.qos.logback" % "logback-classic" % "1.1.7"
  )
) ++ SbtScalariform.scalariformSettings ++ Seq(
  ScalariformKeys.preferences in Compile := formatPreferences,
  ScalariformKeys.preferences in Test := formatPreferences)

val flyway = (project in file("flyway"))
    .settings(commonSettings)
    .settings(
      name := "akka-ddd-sample-flyway"
      , libraryDependencies ++= Seq(
        "com.h2database" % "h2" % "1.4.192"
      )
      , parallelExecution in Test := false
      , flywayUrl := "jdbc:h2:file:./target/todo"
      , flywayUser := "sa"
    )

val db = (project in file("db"))
  .settings(commonSettings)
  .settings(
    name := "akka-ddd-sample-db"
    , libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-stream" % "2.4.10"
      , "com.typesafe.akka" %% "akka-stream-testkit" % "2.4.10" % "test"
      , "com.typesafe.slick" %% "slick" % "3.1.1"
      , "com.typesafe.slick" %% "slick-codegen" % "3.1.1"
      , "com.zaxxer" % "HikariCP" % "2.5.0"
      , "com.h2database" % "h2" % "1.4.192"
    )
    , parallelExecution in Test := false
    , flywayUrl := "jdbc:h2:file:./target/todo"
    , flywayUser := "SA"
  )

val domain = (project in file("domain"))
  .settings(commonSettings)
  .settings(
    name := "akka-ddd-sample-domain"
    , libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor" % "2.4.10"
      , "com.typesafe.akka" %% "akka-cluster" % "2.4.10"
      , "com.typesafe.akka" %% "akka-cluster-sharding" % "2.4.10"
      , "com.typesafe.akka" %% "akka-slf4j" % "2.4.10"
      , "com.typesafe.akka" %% "akka-persistence" % "2.4.10"
      , "com.typesafe.akka" %% "akka-stream" % "2.4.10"
      , "com.typesafe.akka" %% "akka-testkit" % "2.4.10" % "test"
      , "com.typesafe.akka" %% "akka-stream-testkit" % "2.4.10" % "test"
      , "com.typesafe.akka" %% "akka-multi-node-testkit" % "2.4.10" % "test"
      , "com.github.dnvriend" %% "akka-persistence-jdbc" % "2.6.7"
      , "com.h2database" % "h2" % "1.4.192"
      , "org.iq80.leveldb" % "leveldb" % "0.7"
      , "org.fusesource.leveldbjni" % "leveldbjni-all" % "1.8"
    )
  )

val api = (project in file("api"))
  .settings(commonSettings)
  .settings(
    name := "akka-ddd-sample-http"
    , libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http-core" % "2.4.10"
      , "com.typesafe.akka" %% "akka-http-experimental" % "2.4.10"
      , "com.typesafe.akka" %% "akka-http-spray-json-experimental" % "2.4.10"
      , "com.typesafe.akka" %% "akka-testkit" % "2.4.10" % "test"
    )
  ).dependsOn(domain, db)


val readModelUpdater = (project in file("read-model-updater"))
  .settings(commonSettings)
  .settings(
    name := "akka-ddd-sample-read-model-updater"
    , libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-persistence-query-experimental" % "2.4.10"
      , "com.github.dnvriend" %% "akka-persistence-jdbc" % "2.6.7"
      , "com.typesafe.akka" %% "akka-stream" % "2.4.10"
      , "com.typesafe.akka" %% "akka-stream-testkit" % "2.4.10" % "test"
    )
  ).dependsOn(db)

