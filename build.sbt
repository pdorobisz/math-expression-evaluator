name := "math-expression-evaluator"

organization := "com.github.pdorobisz"

version := "1.0.0"

description := "Mathematical expressions evaluator"

scalaVersion := "2.11.4"

libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.2" % "test"

libraryDependencies += "org.scalaz" %% "scalaz-core" % "7.1.0"

libraryDependencies += "org.spire-math" %% "spire" % "0.10.1"

pomExtra in Global := {
  <url>https://github.com/pdorobisz/math-expression-evaluator</url>
    <licenses>
      <license>
        <name>Apache 2</name>
        <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
      </license>
    </licenses>
    <scm>
      <connection>scm:git:git@github.com:pdorobisz/math-expression-evaluator.git</connection>
      <developerConnection>scm:git:git@github.com:pdorobisz/math-expression-evaluator.git</developerConnection>
      <url>git@github.com:pdorobisz/math-expression-evaluator.git</url>
    </scm>
    <developers>
      <developer>
        <id>pdorobisz</id>
        <name>Piotr Dorobisz</name>
        <url>https://github.com/pdorobisz/</url>
      </developer>
    </developers>
}