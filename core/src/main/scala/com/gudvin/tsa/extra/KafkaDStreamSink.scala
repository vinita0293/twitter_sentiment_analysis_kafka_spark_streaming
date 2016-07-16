package com.gudvin.tsa.extra

/**
  * Created by vinita on 7/16/2016 AD.
  */
case class KafkaPayload(key: Option[Array[Byte]], value: Array[Byte])

import org.apache.kafka.clients.producer.{Callback, ProducerRecord, RecordMetadata}
import org.apache.log4j.Logger
import org.apache.spark.TaskContext
import org.apache.spark.streaming.dstream.DStream

class KafkaDStreamSink(dstream: DStream[KafkaPayload]) {

  def sendToKafka(config: Map[String, String], topic: String): Unit = {
    dstream.foreachRDD { rdd =>
      rdd.foreachPartition { records =>
        val producer = KafkaProducer.getOrCreateProducer(config)

        // ugly hack, see: https://github.com/apache/spark/pull/5927
        val context = TaskContext.get
        val logger = Logger.getLogger(getClass)

        val callback = new KafkaDStreamSinkExceptionHandler

        logger.debug(s"Send Spark partition: ${context.partitionId} to Kafka topic: $topic")
        val metadata = records.map { record =>
          callback.throwExceptionIfAny()
          producer.send(new ProducerRecord(topic, record.key.orNull, record.value), callback)
        }.toList

        logger.debug(s"Flush Spark partition: ${context.partitionId} to Kafka topic: $topic")
        metadata.foreach { metadata => metadata.get() }

        callback.throwExceptionIfAny()
      }
    }
  }
}

object KafkaDStreamSink {

  import scala.language.implicitConversions

  implicit def createKafkaDStreamSink(dstream: DStream[KafkaPayload]): KafkaDStreamSink = {
    new KafkaDStreamSink(dstream)
  }

}


class KafkaDStreamSinkExceptionHandler extends Callback {

  import java.util.concurrent.atomic.AtomicReference

  private val lastException = new AtomicReference[Option[Exception]](None)

  override def onCompletion(metadata: RecordMetadata, exception: Exception): Unit = lastException.set(Option(exception))

  def throwExceptionIfAny(): Unit = lastException.getAndSet(None).foreach(ex => throw ex)

}