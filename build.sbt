name := "pa-client"

organization := "com.gu"

version := "1.0-SNAPSHOT"

crossScalaVersions := Seq("2.9.1", "2.9.2")

resolvers += "xuwei-k repo" at "http://xuwei-k.github.com/mvn"

libraryDependencies ++= Seq(
    "net.liftweb" %% "lift-json" % "2.4",
    "net.liftweb" %% "lift-json-ext" % "2.4",
    "joda-time" % "joda-time" % "1.6.2",
    "net.databinder.dispatch" %% "core" % "0.9.0",
    "org.scalatest" %% "scalatest" % "1.8" % "test"
)

publishTo <<= (version) { version: String =>
    val publishType = if (version.endsWith("SNAPSHOT")) "snapshots" else "releases"
    Some(
        Resolver.file(
            "guardian github " + publishType,
            file(System.getProperty("user.home") + "/guardian.github.com/maven/repo-" + publishType)
        )
    )
}