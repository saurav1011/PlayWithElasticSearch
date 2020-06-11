name := """playWithElastic"""
organization := "com.bigphi"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.13.2"

libraryDependencies += guice
// https://mvnrepository.com/artifact/org.elasticsearch/elasticsearch

libraryDependencies += "org.elasticsearch" % "elasticsearch" % "7.7.0" //high level rest client
libraryDependencies += "org.elasticsearch.client" % "elasticsearch-rest-high-level-client" % "7.7.0"

//libraryDependencies += "org.elasticsearch.client" % "rest" % "7.7.0"
//libraryDependencies +="org.elasticsearch.plugin"%"shield"%"7.7.0"
//<dependency>
//  <groupId>com.github.enalmada</groupId>
//  <artifactId>play-elasticsearch_2.11</artifactId>
//  <version>0.1.5</version>
//</dependency>