package bigdataman.baldiberna.naive_bayes_project;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.spark.ml.evaluation.BinaryClassificationEvaluator;
import org.apache.spark.ml.feature.CountVectorizer;
import org.apache.spark.ml.feature.CountVectorizerModel;
import org.apache.spark.ml.feature.IDF;
import org.apache.spark.ml.feature.IDFModel;
import org.apache.spark.ml.feature.StopWordsRemover;
import org.apache.spark.ml.feature.Tokenizer;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.ml.classification.NaiveBayes;
import org.apache.spark.ml.classification.NaiveBayesModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;

public class TrainModel 
{
	public static void main(String[] args) throws IOException{
		
		SparkSession spark = SparkSession.builder().appName("Naive Bayes Train").getOrCreate();
		
		StructType schema = new StructType()
				.add("label", DataTypes.IntegerType, false)
				.add("IDS", DataTypes.IntegerType, false)
				.add("data", DataTypes.StringType, false)
				.add("flag", DataTypes.StringType, false)
				.add("user", DataTypes.StringType, false)
				.add("text", DataTypes.StringType, false);
		
	    Dataset<Row> df = spark.read().option("delimiter", ",").schema(schema).csv("hdfs://"+args[0]);
	    df = df.drop("IDS","date","flag","user");
	    //label | text  remaining
	    df.show(10);
	    Dataset<Row>[] splits = df.randomSplit(new double[]{0.8, 0.2}); //1.280.000 train 320.000 test
	    Dataset<Row> trainingSet = splits[0];
	    Dataset<Row> testSet = splits[1];	    
	    
	    
	    //TOKENIZER
	    Tokenizer tokenizer = new Tokenizer()
	    		.setInputCol("text")
	    		.setOutputCol("tokens");

	    Dataset<Row> tokenizedTrain = tokenizer.transform(trainingSet);
	    Dataset<Row> tokenizedTest = tokenizer.transform(testSet);
	    
	    
	    //STOP WORD
	    StopWordsRemover stopWord = new StopWordsRemover()
	    		.setInputCol("tokens")
	    		.setOutputCol("filteredTokens")
	    		.setStopWords(StopWordsRemover.loadDefaultStopWords("english"));
	    Dataset<Row> filteredTrainSet = stopWord.transform(tokenizedTrain);
	    Dataset<Row> filteredTestSet = stopWord.transform(tokenizedTest);
	    stopWord.write().overwrite().save("hdfs://" + args[1]);
	    
	    
	    //COUNT VECTORIZER
	    CountVectorizerModel cvModel = new CountVectorizer()
	    		.setInputCol("filteredTokens")
	    		.setOutputCol("vectorizedTokens")
	    		.fit(filteredTrainSet);
	    Dataset<Row> vectorizedTrain = cvModel.transform(filteredTrainSet);
	    Dataset<Row> vectorizedTest = cvModel.transform(filteredTestSet);
	    cvModel.write().overwrite().save("hdfs://" + args[2]);
	    
	    
	    //IDF
	    IDFModel idfModel = new IDF()
	    		.setInputCol("vectorizedTokens")
	    		.setOutputCol("features")
	    		.fit(vectorizedTrain);
	    Dataset<Row> weightedTrain = idfModel.transform(vectorizedTrain);
	    Dataset<Row> weightedTest = idfModel.transform(vectorizedTest);
	    idfModel.write().overwrite().save("hdfs://" + args[3]);
	    
	    //NAIVE BAYES
	    NaiveBayesModel nbModel = new NaiveBayes()
	    		.fit(weightedTrain);
	    Dataset<Row> predictionTrain = nbModel.transform(weightedTrain);
	    Dataset<Row> predictionTest = nbModel.transform(weightedTest);
		nbModel.write().overwrite().save("hdfs://" + args[4]);
	   
		
	    //BINARY CLASSIFICATION EVALUATOR
	    BinaryClassificationEvaluator evaluator = new BinaryClassificationEvaluator()
	    		.setLabelCol("label")
	    		.setRawPredictionCol("prediction");
	    double accuracyTrain = evaluator.evaluate(predictionTrain);
	    double accuracyTest = evaluator.evaluate(predictionTest);
	    
	    List<String> data = Arrays.asList(
	    		"Accuracy train: " + accuracyTrain + "\n"
	    				+ "Accuracy test: " + accuracyTest + "\n");
	    JavaSparkContext javaContext = new JavaSparkContext(spark.sparkContext());
	    JavaRDD<String> distData = javaContext.parallelize(data);
	    distData.saveAsTextFile("hdfs://" + args[5]);

	    javaContext.close();
	    spark.close();
	}

}
