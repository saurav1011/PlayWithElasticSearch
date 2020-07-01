name := """playWithElastic"""
organization := "com.bigphi"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.13.2"

libraryDependencies += "com.google.code.gson" % "gson" % "2.3.1"

libraryDependencies += guice

libraryDependencies += "org.elasticsearch" % "elasticsearch" % "7.7.0" //high level rest client
libraryDependencies += "org.elasticsearch.client" % "elasticsearch-rest-high-level-client" % "7.7.0"

// https://mvnrepository.com/artifact/org.apache.commons/commons-collections4
libraryDependencies += "org.apache.commons" % "commons-collections4" % "4.1"

routesGenerator := InjectedRoutesGenerator
