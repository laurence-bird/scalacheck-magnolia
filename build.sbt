name := "magnolia_blog"

version := "1.0"

scalaVersion := "2.12.4"

libraryDependencies ++= Seq(
  "com.propensive" %% "magnolia"    % "0.6.1",
  "org.scalacheck" %% "scalacheck"  % "1.13.5",
  "org.scalatest" %% "scalatest"    % "3.0.4" % Test
)