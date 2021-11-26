# BigData-Project

A spark-based application for tweet's *sentiment analysis*, running inside an Hadoop cluster

# Intentions
With this project, we wanted to explore usage some of the most popular softwares for *Big Data managment*. 
In detail, we've used **Apache Hadoop** for build-up a 3-node cluster (**with HDFS as FS**) and we use **Apache Spark** above them whith MLlib, a Spark library for design machine learning's models.
For task (*given a tweet/phrase, choose if it's a positive or negative comment*), we've choose to use **Naive Bayes classifer**: the perfect trade off between simplicity and performance. Thanks by an a 
simple (and quite incorrect for documents) hypotesis: the features (in this case words) of a sample (in this case a text/tweet) are *independent random variables*.  Altough in a text words might be correlated, this
model provide good performance!

As Dataset, the [Μαριος Μιχαηλιδης KazAnova dataset](https://www.kaggle.com/kazanova/sentiment140) was perfect: tons of labeled tweet (**1.6 millions of tweets!!**) for justify the distributed approach and high usability.
Below a snippet of the dataset
| Target | ids | date | flag | user | text|
| --- | --- | --- | --- | --- | --- |
|0|"1467810369"|"Mon Apr 06 22:19:45 PDT 2009"|"NO_QUERY"|"_TheSpecialOne_"|"@switchfoot http://twitpic.com/2y1zl - Awww, that's a bummer.  You shoulda got David Carr of Third Day to do it. 	D"|									
|0|"1467810672"|"Mon Apr 06 22:19:49 PDT 2009"|"NO_QUERY"|"scotthamilton"|"is upset that he can't update his Facebook by texting it... and might cry as a result  School today also. Blah!"				|						
<!--
|0|"1467810917"|"Mon Apr 06 22:19:53 PDT 2009"|"NO_QUERY"|"mattycus"|"@Kenichan I dived many times for the ball. Managed to save 50%  The rest go out of bounds"										|
|0|"1467811184"|"Mon Apr 06 22:19:57 PDT 2009"|"NO_QUERY"|"ElleCTF"|"my whole body feels itchy and like its on fire "										|
-->

* target: the polarity of the tweet (0 = negative, 1 = positive)
* ids: The id of the tweet ( 2087)
* date: the date of the tweet (Sat May 16 23:58:44 UTC 2009)
* flag: The query (lyx). If there is no query, then this value is NO_QUERY.
* user: the user that tweeted
* text: the text of the tweet

For the application, only *Target* and *Text* colums are needed.


# SetUp cluster
## Requirements
- [Apache Spark 3.0.3](https://spark.apache.org/releases/spark-release-3-0-3.html)
- [Apache Hadoop 3.2.2](https://hadoop.apache.org/docs/r3.2.2/)
- [Apache MLlib](https://spark.apache.org/mllib/)
- Java 8 (We know, it's weird use Java for an ML task :-) )








