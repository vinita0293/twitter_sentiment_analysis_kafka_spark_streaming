package com.gudvin.tsa.extra

import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.log4j.Logger

import scala.collection.mutable

object KafkaProducer {

  import scala.collection.JavaConversions._

  private val logger = Logger.getLogger(getClass)

  private val producers = mutable.Map[Map[String, String], KafkaProducer[Array[Byte], Array[Byte]]]()

  def getOrCreateProducer(config: Map[String, String]): KafkaProducer[Array[Byte], Array[Byte]] = {

    val defaultConfig = Map(
      "key.serializer" -> "org.apache.kafka.common.serialization.ByteArraySerializer",
      "value.serializer" -> "org.apache.kafka.common.serialization.ByteArraySerializer"
    )

    val finalConfig = defaultConfig ++ config

    producers.getOrElseUpdate(finalConfig, {
      logger.info(s"Create Kafka publisher , config: $finalConfig")
      val producer = new KafkaProducer[Array[Byte], Array[Byte]](finalConfig)

      sys.addShutdownHook {
        logger.info(s"Close Kafka publisher, config: $finalConfig")
        producer.close()
      }

      producer
    })
  }
}
