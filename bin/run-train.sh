#!/bin/bash 


hdfs dfsadmin -safemode leave

# delete all model 
hdfs dfs -rm -r models

# delete output
hdfs dfs -rm -r output

# load dataset into hdfs
hdfs dfs -put /home/hadoopuser/esProf/dataset dataset

# create dir
hdfs dfs -mkdir  models

# submit
/usr/local/spark/bin/spark-submit --class bigdataman.baldiberna.naive_bayes_project.TrainModel --master yarn  --deploy-mode cluster \
 --num-executors 3 --driver-memory 2g  --executor-memory 3g  --executor-cores 2  --queue default \
/home/hadoopuser/naive_bayes_project/naive_bayes_project.jar \
	/user/hadoopuser/dataset \
	/user/hadoopuser/models/stopWordModel \
	/user/hadoopuser/models/countVectorizerModel \
	/user/hadoopuser/models/idfModel \
	/user/hadoopuser/models/NaiveBayesModel \
	/user/hadoopuser/output


# mkdir output

# get accuracy model from hdfs
hdfs dfs -get output /home/hadoopuser/output
