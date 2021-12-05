#!/bin/bash 


rm -r /home/hadoopuser/esProf/output
hdfs dfsadmin -safemode leave

#hdfs dfs -rm -r dataset
#hdfs dfs -rm -r models
#hdfs dfs -rm -r models/stopWordModel
#hdfs dfs -rm -r models/countVectorizerModel
#hdfs dfs -rm -r models/idfModel
#hdfs dfs -rm -r models/NaiveBayesModel
#hdfs dfs -rm -r output


#hdfs dfs -put /home/hadoopuser/esProf/dataset dataset
#hdfs dfs -touch output

#hdfs dfs -mkdir  models
#hdfs dfs -mkdir  models/stopWordModel
#hdfs dfs -mkdir  models/countVectorizerModel
#hdfs dfs -mkdir  models/idfModel
#hdfs dfs -mkdir  models/NaiveBayesModel


/usr/local/spark/bin/spark-submit --class bigdataman.baldiberna.naive_bayes_project.UserTest --master yarn  --deploy-mode client \
 --num-executors 3 --driver-memory 2g  --executor-memory 3g  --executor-cores 2  --queue default \
/home/hadoopuser/naive_bayes_project/naive_bayes_project.jar \
	/user/hadoopuser/models/stopWordModel \
	/user/hadoopuser/models/countVectorizerModel \
	/user/hadoopuser/models/idfModel \
	/user/hadoopuser/models/NaiveBayesModel \

