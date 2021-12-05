package bigdataman.baldiberna.naive_bayes_project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import org.apache.spark.ml.classification.NaiveBayesModel;
import org.apache.spark.ml.feature.CountVectorizerModel;
import org.apache.spark.ml.feature.IDFModel;
import org.apache.spark.ml.feature.StopWordsRemover;
import org.apache.spark.ml.feature.Tokenizer;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

public class UserTest {

	public static void main(String[] args) {
		
		SparkSession spark = SparkSession.builder().appName("Naive Bayes Test").getOrCreate();
		
		Tokenizer tokenizer = new Tokenizer()
	    		.setInputCol("text")
	    		.setOutputCol("tokens");
		StopWordsRemover stopWord = StopWordsRemover.load("hdfs://" + args[0]);
		CountVectorizerModel cvModel = CountVectorizerModel.load("hdfs://" + args[1]);
		IDFModel idfModel = IDFModel.load("hdfs://" + args[2]);
		NaiveBayesModel nbModel = NaiveBayesModel.load("hdfs://" + args[3]);
		
		String input = "";
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("*************************************");
		System.out.println("****  Sentiment analisys shell  *****");
		System.out.println("*************************************");
		System.out.println("*************************************");
		System.out.println("****  BaldiBerna Inc. 2021      *****");
		System.out.println("*************************************");

		
		do {
			System.out.println("Insert a sentence (Q to quit)");
			try {
				input = bufferedReader.readLine();
				if(!input.equals("Q") && !input.equals("")) {
					
					List<Row> data = Arrays.asList(RowFactory.create(input));
					StructType schema = new StructType(new StructField[]{
							new StructField("text", DataTypes.StringType, false, Metadata.empty())
					});
					Dataset<Row> testSet = spark.createDataFrame(data, schema);
					
				    Dataset<Row> tokenizedTest = tokenizer.transform(testSet);
				    Dataset<Row> filteredTestSet = stopWord.transform(tokenizedTest);
				    Dataset<Row> vectorizedTest = cvModel.transform(filteredTestSet);
				    Dataset<Row> weightedTest = idfModel.transform(vectorizedTest);
				    Dataset<Row> predictionTest = nbModel.transform(weightedTest);
				    
				    predictionTest.show();
				    String prediction = predictionTest.select("prediction").first().get(0).toString();
				    if(Double.parseDouble(prediction) == 1.0) {
				    	System.out.println("Yess! It's a good sentence");
				    }else {
				    	System.out.println("Oh no! It's a bad sentence");
				    }
				}
			}catch(IOException e){
			}
		}while(!input.equals("Q"));
		System.out.println("Closing application. See you soon!!");
	}
}
