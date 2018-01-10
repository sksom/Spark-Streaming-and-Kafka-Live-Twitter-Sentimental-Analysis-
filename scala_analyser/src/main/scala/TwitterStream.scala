import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010._

object TwitterStream {

  def main(args: Array[String]) {
    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> "localhost:9092",
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> "use_a_separate_group_id_for_each_stream",
      "auto.offset.reset" -> "latest",
      "enable.auto.commit" -> (false: java.lang.Boolean)
    )
    val sparkConf = new SparkConf().setMaster("local[2]").setAppName("tweeter")
    val ssc = new StreamingContext(sparkConf, Seconds(2))
    val topics = Array("twitterstream")     //twitterstream is the zookeeper topic name
    //Here we create a DStream of the tweets from the zookeeper topic "twitterstream"
    val stream = KafkaUtils.createDirectStream[String, String](
      ssc,
      PreferConsistent,
      Subscribe[String, String](topics, kafkaParams)
    )
    stream.foreachRDD { rdd =>
      rdd.foreach { record =>
        val value = record.value()
        val tweet = scala.util.parsing.json.JSON.parseFull(value)
        val map:Map[String,Any] = tweet.get.asInstanceOf[Map[String, Any]]
        val sntmnt=SentimentAnalysisUtils.detectSentiment(map.get("text").toString)
        println(map.get("text")+" SENTIMENT:"+sntmnt.toString)
        //println("THIS IS A TEST MESSAGE TO CHECK WHETHER THE PROGRAM IS RUNNING FINE OR NOT")
      }
    }

    ssc.start()
    ssc.awaitTermination()
  }
}
