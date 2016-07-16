package com.gudvin.tsa.spark.subscriber

import kafka.serializer.StringDecoder
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by vinita on 7/12/16.
  */
object SparkKafkaStreamingConsumer {
  def main(args: Array[String]) {
    val sparkHome = "/usr/local/spark-1.6.1-hadoop2.6-firsttime/"
    val sparkMasterUrl = "spark://vinita-Lenovo-G50-80:7077"

    val conf: SparkConf = new SparkConf()
      .setAppName("Spark Kafka Streaming Consumer")
      .setMaster("local[6]")
      .setSparkHome(sparkHome)

    val KAFKA_BROKERS = "localhost:9092"
    val KAFKA_TOPICS =Set("chattarpattar")
    val kafkaParams = Map("metadata.broker.list" -> KAFKA_BROKERS,
      "zookeeper.connect" -> "localhost:2181",
      "group.id" -> "kafka-spark-streaming-example",
      "zookeeper.connection.timeout.ms" -> "1000"
    )

    val sc = new SparkContext(conf)
    val ssc = new StreamingContext(sc, Seconds(10))

    val kafkaStream = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](
      ssc, kafkaParams, KAFKA_TOPICS
    )

    kafkaStream.print()

    ssc.start()
    ssc.awaitTermination()
    }

  }
