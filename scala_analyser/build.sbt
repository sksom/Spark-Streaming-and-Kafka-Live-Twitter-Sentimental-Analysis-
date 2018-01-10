name := "HW_3"
version := "1.0"
libraryDependencies ++= {
  val sparkVer = "2.2.0"
  Seq(
    "org.apache.spark" %% "spark-core" % sparkVer,
    "org.apache.spark" %% "spark-mllib" % sparkVer,
    "org.apache.spark" %% "spark-streaming" % sparkVer,
    "org.apache.spark" % "spark-streaming-kafka-0-10_2.11" % sparkVer,
    "org.apache.kafka" % "kafka-clients" % "0.10.2.1",
    "edu.stanford.nlp" % "stanford-corenlp" % "3.5.1",
    "edu.stanford.nlp" % "stanford-corenlp" % "3.5.1" classifier "models"
  )
}
scalaVersion := "2.11.8"