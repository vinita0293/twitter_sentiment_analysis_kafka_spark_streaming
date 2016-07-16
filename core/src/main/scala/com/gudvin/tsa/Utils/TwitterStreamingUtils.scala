package com.gudvin.tsa.Utils

/**
  * Created by vinita on 7/16/2016 AD.
  */
object TwitterStreamingUtils {

  def setOAuthCredentials: Unit ={

    val consumerKey = "CyuGlzTbWsD9LNrjyJavvnt88"
    val consumerSecret = "7z4S8LoPJanekLxyzBKD7wfAbm2SgDX1zrJ36Bfcis7uqVfhG0"
    val accessToken = "2217026450-NzTHo0kwcao7swmJLD1retrLT7m4d0ONhQL1n5o"
    val accessTokenSecret = "DbSIq0MP5S2nAcZQOd6MfEGu08zqhRCYTDCS849gEt8Md"

    System.setProperty("twitter4j.oauth.consumerKey", consumerKey)
    System.setProperty("twitter4j.oauth.consumerSecret", consumerSecret)
    System.setProperty("twitter4j.oauth.accessToken", accessToken)
    System.setProperty("twitter4j.oauth.accessTokenSecret", accessTokenSecret)
  }
}
