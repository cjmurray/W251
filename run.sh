#!/usr/bin/bash

topN=20
shortSampleSec=60
longSampleMin=5

$SPARK_HOME/bin/spark-submit   --master spark://spark1:7077 $(find target -iname "*assembly*.jar")   $topN $shortSampleSec $longSampleMin
