package com.gudvin.tsa.Utils

import java.util.Properties

import edu.stanford.nlp.ling.CoreAnnotations
import edu.stanford.nlp.pipeline.StanfordCoreNLP
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations
import edu.stanford.nlp.util.CoreMap

/**
  * Created by vinita on 7/15/16.
  */
object NLPUtils {

  def getPipeline(): StanfordCoreNLP ={
    val props = new Properties()
    props.setProperty("annotators",
      "tokenize, ssplit, pos, lemma, parse, sentiment")
    new StanfordCoreNLP(props)
  }

  def detectSentiment(message: String, pipeline: StanfordCoreNLP) = {

    val annotation = pipeline.process(message)
    val sentences = annotation
      .get(classOf[CoreAnnotations.SentencesAnnotation])
    val sentiments = sentences.toArray.map(_.asInstanceOf[CoreMap].get(classOf[SentimentCoreAnnotations.ClassName]) match {
      case "Positive" => 1
      case "Negative" => -1
      case _ => 0
    })

    sentiments.reduce(_+_)/sentiments.length
  }
}
