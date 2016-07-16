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

/**
 * Calculates popular hashtags (topics) over sliding 10 and 60 second windows from a Twitter
 * stream. The stream is instantiated with credentials and optionally filters supplied by the
 * command line arguments.
 *
 */

object twitter_popularity {
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

	// extract hashtags and usernames
    val hashTags = stream.flatMap(status => status.getText.split(" ").filter(_.startsWith("#")))
    val mentions = stream.flatMap(status => status.getText.split(" ").filter(_.startsWith("@")).filter(_.length > 1))
    val authors = stream.flatMap(status => status.getUser.getScreenName().split(" "))

	// concatenate a single list of users
	val users = authors
	
    val topHashTags60 = hashTags.map((_, 1)).reduceByKeyAndWindow(_ + _, Seconds(executionTime))
                     .map{case (topic, count) => (count, topic)}
                     .transform(_.sortByKey(false))

    val topUsers60 = users.map((_, 1)).reduceByKeyAndWindow(_ + _, Seconds(executionTime))
                     .map{case (user, count) => (count, user)}
                     .transform(_.sortByKey(false))

    val topHashTags10 = hashTags.map((_, 1)).reduceByKeyAndWindow(_ + _, Seconds(sampleDuration))
                     .map{case (topic, count) => (count, topic)}
                     .transform(_.sortByKey(false))

    val topUsers10 = users.map((_, 1)).reduceByKeyAndWindow(_ + _, Seconds(sampleDuration))
                     .map{case (user, count) => (count, user)}
                     .transform(_.sortByKey(false))

    // Print popular hashtags
    topHashTags60.foreachRDD(rdd => {
      val topList = rdd.take(topN)
      println("\nPopular topics in last %d seconds (%s total):".format(executionTime, rdd.count()))
      topList.foreach{case (count, tag) => println("%s (%s tweets)".format(tag, count))}
    })

    topUsers60.foreachRDD(rdd => {
      val topList = rdd.take(topN)
      println("\nPopular users in last %d seconds (%s total):".format(executionTime, rdd.count()))
      topList.foreach{case (count, tag) => println("@%s (%s tweets)".format(tag, count))}
    })

    topHashTags10.foreachRDD(rdd => {
      val topList = rdd.take(topN)
      println("\nPopular topics in last %d seconds (%s total):".format(sampleDuration, rdd.count()))
      topList.foreach{case (count, tag) => println("%s (%s tweets)".format(tag, count))}
    })

    topUsers10.foreachRDD(rdd => {
      val topList = rdd.take(topN)
      println("\nPopular users in last %d seconds (%s total):".format(sampleDuration, rdd.count()))
      topList.foreach{case (count, tag) => println("@%s (%s tweets)".format(tag, count))}
    })

    ssc.start()
    ssc.awaitTermination()
  }
}
// scalastyle:on println
