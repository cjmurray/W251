#!/usr/bin/bash

topN=20
shortSampleSec=120
longSampleMin=30

$SPARK_HOME/bin/spark-submit   --master spark://spark1:7077 $(find target -iname "*assembly*.jar")   $topN $shortSampleSec $longSampleMin
