lazy val `ga-us-distribution-insights` = (project in file(".")).
  settings(
    name := "ga-us-gdh-bi-effectiveness-feed",
    organization:= "com.beamsuntory.gdh_bi_effectiveness_feed.us",
    version := "1.0.0-00-SNAPSHOT",
    scalaVersion := "2.12.14",
    mainClass in Compile := Some("myPackage.GdhLoadBiEffectiveness")
  )

val sparkVersion = "3.1.3"

// disable using the Scala version in output paths and artifacts
crossPaths := false

parallelExecution in Test := false

//option to avoid warnings
updateOptions := updateOptions.value.withLatestSnapshots(false)

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion % "provided",
  "org.apache.spark" %% "spark-sql" % sparkVersion % "provided",
  "org.apache.spark" %% "spark-hive" % sparkVersion % "provided",
  "junit" % "junit" % "4.12" % "test",
  "com.novocode" % "junit-interface" % "0.11" % "test",
  "com.beamsuntory.bgc.commons" % "commons" % "2.0.2-11-SNAPSHOT",
  "com.novocode" % "junit-interface" % "0.11" % Test,
  "junit" % "junit" % "4.12" % Test,
  "org.scalatest" %% "scalatest" % "3.0.8" % Test,
  "log4j" % "log4j" % "1.2.17",
  "com.crealytics" % "spark-excel_2.12" % "0.13.5"
)

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", "services", xs @ _*)  => MergeStrategy.filterDistinctLines
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}

artifact in (Compile, assembly) := {
  val art = (artifact in (Compile, assembly)).value
  art.copy(`classifier` = Some("assembly"))
}

addArtifact(artifact in (Compile, assembly), assembly)

sources in (Compile,doc) := Seq.empty
publishArtifact in (Compile, packageDoc) := false

val nexusHost=sys.props.getOrElse("nexus.host", "https://nexus.global.bgsw.com:8081")

credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")

resolvers += "Nexus-Snapshot" at s"$nexusHost/repository/gcp_etl_maven_snapshots/"
resolvers += "Nexus-Releases" at s"$nexusHost/repository/gcp_etl_maven_release/"

publishTo := {
  val nexus = s"$nexusHost/"
  if (version.value.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "repository/gcp_etl_maven_snapshots/")
  else
    Some("releases"  at nexus + "repository/gcp_etl_maven_release/")
}
