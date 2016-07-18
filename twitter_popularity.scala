/*
 * W251 HW9 - Streaming Tweet Processing
 * Chris Murray
 */

// scalastyle:off println
package org.apache.spark.examples.streaming

import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.SparkContext._
import org.apache.spark.streaming.twitter._
import org.apache.spark.SparkConf

import scala.collection.mutable.ListBuffer

/**
 * Calculates popular hashtags (topics) over sliding 10 and 60 second windows from a Twitter
 * stream. The stream is instantiated with credentials and optionally filters supplied by the
 * command line arguments.
 *
 */

object twitter_popularity {

  def printStuff() {
	println("\nStuff")
  }

  def main(args: Array[String]) {

	if (args.length < 3) {
		System.err.println("Usage: twitter_popularity <sampleDurationSeconds> <top_n_hashtags> <executionTimeMinutes>")
		System.exit(1)
	}

	var sampleDuration = args(0).toInt
	var topN = args(1).toInt
	var executionTime = args(2).toInt * 60

	println(s"sampleDuration=${sampleDuration}, topN=${topN}, executionTime=${executionTime}")
	
	// Twitter credentials moved to twitter4j.properties

	// don't filter out any tweets
	val filters = Array[String]()

	// get a Twitter stream
	val sparkConf = new SparkConf().setAppName("twitter_popularity")
    val ssc = new StreamingContext(sparkConf, Seconds(sampleDuration))
    val stream = TwitterUtils.createStream(ssc, None, filters)
		
	// extract hashtags
    val hashtags = stream.flatMap(status => status.getText.split(" ").filter(_.startsWith("#")))
			
	// count hashtags
    val hashtagCount = hashtags.map((_, 1)).reduceByKeyAndWindow(_ + _, Seconds(sampleDuration))
		 .map{case (topic, count) => (count, topic)}
		 .transform(_.sortByKey(false))

	var topHashtags = new ListBuffer[String]()
	
	hashtagCount.foreachRDD(rdd => {
		val topHashtagCount = rdd.take(topN)
		topHashtagCount.foreach{case (count, tag) => topHashtags += tag}
		println("topHashtags: " + topHashtags)
	})
	
	val hashtagAuthors = stream.map(status => {
			val hashtags = status.getText.split(" ").filter(_.startsWith("#"))
			val author = "@" + status.getUser.getScreenName
			hashtags.foreach{tag =>
				(tag, author)
			}
		})
		// .reduceByKeyAndWindow(_.mkString(",", Seconds(sampleDuration)))
	
	hashtagAuthors.foreachRDD(rdd => {
		println("hashtagAuthors: " + rdd.collect)
	})
	
	

	// val containsThe = stream.map{ status => 
		// if (status.getText.contains("the"))
			// status.getText
	// }

	// containsThe.foreachRDD(rdd => {
		// println("dig")
		// rdd.collect.foreach{s =>
			// println(s)
		// }
	// })
	
	// get all the tweets containing popular hashtags
    // val hashtags = stream.flatMap(status => status.getText.split(" ").filter(_.startsWith("#")))
	
	// find authors of tweets of popular hashtags
    // val authors = stream.flatMap(status => status.getUser.getScreenName().split(" ").map(s => "@"+s))

	// find mentions in tweets with popular hashtags

	
    ssc.start()
    ssc.awaitTermination()
  }
}
// scalastyle:on println
