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
		System.err.println("Usage: twitter_popularity <top_n_hashtags> <shortSampleDurationSeconds> <longSampleDurationMinutes>")
		System.exit(1)
	}

	var topN = args(0).toInt
	var shortSampleDuration = args(1).toInt
	var longSampleDuration = args(2).toInt * 60

	println(s"topN=${topN}, shortSampleDuration=${shortSampleDuration}, longSampleDuration=${longSampleDuration}")
	
	// Twitter credentials moved to twitter4j.properties

	// don't filter out any tweets
	val filters = Array[String]()

	// get a Twitter stream
	val sparkConf = new SparkConf().setAppName("twitter_popularity")
    val ssc = new StreamingContext(sparkConf, Seconds(shortSampleDuration))
    val stream = TwitterUtils.createStream(ssc, None, filters)
		
	// create hashtag:author pairs
	val hashtagAuthorsShort = stream.flatMap(status => {
			val author = "@" + status.getUser.getScreenName
			val hashtags = status.getText.split(" ").filter(_.startsWith("#"))
			hashtags.map(tag =>	(tag, author))
		}).reduceByKeyAndWindow(_ + "," + _, Seconds(shortSampleDuration))
		
	// create count:hashtag-authors pairs
	val countHashtagAuthorsShort = hashtagAuthorsShort.map{case (tag, authors) =>
		val count = authors.split(",").length
		(count, tag + " " + authors)
	}.transform(_.sortByKey(false))
	
	// print out top N hashtags
	countHashtagAuthorsShort.foreachRDD(rdd => {
		val topList = rdd.take(topN)
		println("\nPopular topics in last %d seconds (%s total):\n".format(shortSampleDuration, rdd.count()))
		topList.foreach{case (count, tagAuthors) => 
			val fields = tagAuthors.split(" ")
			println("Hashtag: %s (%s tweets)".format(fields(0), count))
			println("Authors: %s".format(fields(1)))
			println("-"*10)
		}
	})
	
	// create hashtag:author pairs
	val hashtagAuthorsLong = stream.flatMap(status => {
			val author = "@" + status.getUser.getScreenName
			val hashtags = status.getText.split(" ").filter(_.startsWith("#"))
			hashtags.map(tag =>	(tag, author))
		}).reduceByKeyAndWindow(_ + "," + _, Seconds(shortSampleDuration))
		
	// create count:hashtag-authors pairs
	val countHashtagAuthorsLong = hashtagAuthorsLong.map{case (tag, authors) =>
		val count = authors.split(",").length
		(count, tag + " " + authors)
	}.transform(_.sortByKey(false))
	
	// print out top N hashtags
	countHashtagAuthorsLong.foreachRDD(rdd => {
		val topList = rdd.take(topN)
		println("\nPopular topics in last %d seconds (%s total):\n".format(shortSampleDuration, rdd.count()))
		topList.foreach{case (count, tagAuthors) => 
			val fields = tagAuthors.split(" ")
			println("Hashtag: %s (%s tweets)".format(fields(0), count))
			println("Authors: %s".format(fields(1)))
			println("-"*10)
		}
	})

    ssc.start()
    ssc.awaitTermination()
  }
}
// scalastyle:on println
