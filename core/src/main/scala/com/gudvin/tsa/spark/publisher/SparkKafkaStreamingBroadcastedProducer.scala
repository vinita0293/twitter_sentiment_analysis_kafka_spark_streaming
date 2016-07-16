package com.gudvin.tsa.spark.publisher

import com.gudvin.tsa.Utils.TwitterStreamingUtils
import com.gudvin.tsa.kafka.KafkaSerializedProducer
import org.apache.spark.streaming.twitter.TwitterUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by vinita on 7/12/16.
  */
object SparkKafkaStreamingBroadcastedProducer {
  def main(args: Array[String]) {
    val sparkHome = "/usr/local/spark-1.6.1-hadoop2.6-firsttime/"
    val sparkMasterUrl = "spark://vinita-Lenovo-G50-80:7077"

    val conf: SparkConf = new SparkConf()
      .setAppName("Spark Kafka Streaming Producer")
      .setMaster("local[6]")
      .setSparkHome(sparkHome)
    val ip = "localhost"
    val KAFKA_TOPIC = "chattarpattar"

    val sc = new SparkContext(conf)
    val ssc = new StreamingContext(sc, Seconds(10))

    TwitterStreamingUtils.setOAuthCredentials

    val twitterStream = TwitterUtils.createStream(ssc, None, Seq("obama"))
    val broadCastedKafkaProducer = sc.broadcast(KafkaSerializedProducer())
    twitterStream.foreachRDD(rdd => {

      rdd.foreach(status =>
        broadCastedKafkaProducer.value.publishToKafka(KAFKA_TOPIC, ip, status.toString))
    })

    ssc.start()
    ssc.awaitTermination()
  }
}
