package com.gudvin.tsa.kafka

import java.util.Properties

import kafka.producer.{KeyedMessage, Producer, ProducerConfig}

/**
  * Created by vinita on 7/16/2016 AD.
  */

class KafkaSerializedProducer(createProducer: () =>  Producer[String, String]) extends Serializable {

  lazy val producer = createProducer()

  def publishToKafka(topic: String, ip: String, message: String): Unit =
    producer.send(new KeyedMessage[String, String](topic, ip, message))
}

object KafkaSerializedProducer {
  def apply(): KafkaSerializedProducer = {
    val f = () => {
      val config = new ProducerConfig(setProperties)
      new Producer[String, String](config)
    }
    new KafkaSerializedProducer(f)
  }

  def setProperties: Properties ={
    val props = new Properties()

    val KAFKA_BROKERS = "localhost:9092"
    props.put("metadata.broker.list", KAFKA_BROKERS)
    props.put("serializer.class", "kafka.serializer.StringEncoder")
    props.put("request.required.acks", "1")

    props
  }
}