#!/bin/bash 



hdfs dfsadmin -safemode leave


/usr/local/spark/bin/spark-submit --class bigdataman.baldiberna.naive_bayes_project.UserTest --master yarn  --deploy-mode client \
 --num-executors 3 --driver-memory 2g  --executor-memory 3g  --executor-cores 2  --queue default \
/home/hadoopuser/naive_bayes_project/naive_bayes_project.jar \
	/user/hadoopuser/models/stopWordModel \
	/user/hadoopuser/models/countVectorizerModel \
	/user/hadoopuser/models/idfModel \
	/user/hadoopuser/models/NaiveBayesModel \

