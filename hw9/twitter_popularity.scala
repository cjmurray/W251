/*
 * W251 HW9 - Streaming Tweet Processing
 * Chris Murray
 */

package org.apache.spark.examples.streaming

import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.SparkContext._
import org.apache.spark.streaming.twitter._
import org.apache.spark.SparkConf

import scala.collection.mutable.ListBuffer

object twitter_popularity {

  // define class to hold data for a given hashtag
  case class TagData(tag:String, authors:Array[String], mentions:Array[String]) {
	def +(other:TagData) : TagData = {
		val allAuthors = authors ++ other.authors
		val allMentions = mentions ++ other.mentions
		new TagData(tag, allAuthors.distinct, allMentions.distinct)
	}
  }

  def main(args: Array[String]) {

	// get command line arguments
	if (args.length < 3) {
		System.err.println("Usage: twitter_popularity <top_n_hashtags> <sampleDurationShortSeconds> <sampleDurationLongMinutes>")
		System.exit(1)
	}

	var topN = args(0).toInt
	var sampleDurationShort = args(1).toInt
	var sampleDurationLong = args(2).toInt * 60

	println(s"topN=${topN}, sampleDurationShort=${sampleDurationShort}, sampleDurationLong=${sampleDurationLong}")
	
	// don't filter out any tweets
	val filters = Array[String]()

	// get a Twitter stream
	// note: Twitter credentials are in twitter4j.properties
	val sparkConf = new SparkConf().setAppName("twitter_popularity")
    val ssc = new StreamingContext(sparkConf, Seconds(sampleDurationShort))
    val stream = TwitterUtils.createStream(ssc, None, filters)
		
	// create hashtag:TagData pairs for short duration
	val hashTagDataShort = stream.flatMap(status => {
			val author = "@" + status.getUser.getScreenName
			val hashtags = status.getText.split("\\s+").filter(_.startsWith("#")).filter(_.length > 1)
			val mentions = status.getUserMentionEntities.map("@" + _.getScreenName)
			hashtags.map(tag =>	(tag, new TagData(tag, Array(author), mentions)))
		}).reduceByKeyAndWindow(_ + _, Seconds(sampleDurationShort))

	// create hashtag:TagData pairs for long duration
	val hashTagDataLong = stream.flatMap(status => {
			val author = "@" + status.getUser.getScreenName
			val hashtags = status.getText.split("\\s+").filter(_.startsWith("#")).filter(_.length > 1)
			val mentions = status.getUserMentionEntities.map("@" + _.getScreenName)
			hashtags.map(tag =>	(tag, new TagData(tag, Array(author), mentions)))
		}).reduceByKeyAndWindow(_ + _, Seconds(sampleDurationLong))

	// create count:TagData pairs for short duration
	val countTagDataShort = hashTagDataShort.map{case (tag, data) =>
		val count = data.authors.length
		(count, data)
	}.transform(_.sortByKey(false))
	
	// create count:TagData pairs for long duration
	val countTagDataLong = hashTagDataLong.map{case (tag, data) =>
		val count = data.authors.length
		(count, data)
	}.transform(_.sortByKey(false))
	
	// print out top N hashtags for short duration
	countTagDataShort.foreachRDD(rdd => {
		val topList = rdd.take(topN)
		println("\nPopular topics in last %d seconds (%s total):\n".format(sampleDurationShort, rdd.count()))
		topList.foreach{case (count, data) => 
			val authors = data.authors.mkString(",")
			val mentions = data.mentions.mkString(",")
			println("Hashtag:  %s (%s tweets)".format(data.tag, count))
			println("Authors:  " + authors)
			if (mentions != "") 
				println("Mentions: " + mentions)
			println()
		}
	})

	// print out top N hashtags for long duration
	countTagDataLong.foreachRDD(rdd => {
		val topList = rdd.take(topN)
		println("\nPopular topics in last %d seconds (%s total):\n".format(sampleDurationLong, rdd.count()))
		topList.foreach{case (count, data) => 
			val authors = data.authors.mkString(",")
			val mentions = data.mentions.mkString(",")
			println("Hashtag:  %s (%s tweets)".format(data.tag, count))
			println("Authors:  " + authors)
			if (mentions != "") 
				println("Mentions: " + mentions)
			println()
		}
	})

    ssc.start()
    ssc.awaitTermination()
  }
}
