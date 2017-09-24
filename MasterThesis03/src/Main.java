import java.io.*;
import java.text.DecimalFormat;
import java.util.Scanner;

import KNN.KNNKind;
import KNN.KNNModel;
import Naive.*;
import kind.*;
import util.*;
import metadata.*;

public class Main {
	final static String PATH="Data";

	@SuppressWarnings("static-access")
	public static void main(String[] args) throws IOException {
		ClassificationModel model ;
		Classifier tree_classifier;
		Scanner scan = new Scanner(System.in);
		Stopwatch stopwatch = new Stopwatch();
		FileOutputStream file_output;
		DataOutputStream data_out;
		
		//cycle through all the problems
		ProblemContainer container = new ProblemContainer(PATH);
		String[] file_paths = container.getProblemPaths();

		System.out.println("pleaseselect the method type:\n0 knn\n1 Naive Bayes");
		int type = scan.nextInt();

		if(type==0){

			System.out.println("Please Enter a K for Number of Neighbors: ");
			System.out.println("**for PERCENT use % with desired number(e.g. 5%)**");
			String str = scan.next();

			System.out.println("Please Enter a Number for distance: \n0= Euclidan\\overlaping \n"
					+ "1= Euclidan\\probability \n2= Manhatan\\overlaping \n3= Manhatan\\Probability");
			int d = scan.nextInt();
			File file = new File("out/knn"+str+"-"+d+"result.xls");
			// Create an output stream to the file.
			file_output = new FileOutputStream (file);
			// Wrap the FileOutputStream with a DataOutputStream
			data_out = new DataOutputStream (file_output);

			for (String name: file_paths){
				Problem problem = new Problem(PATH+"//"+name+"//" + name);
				System.out.println("\nProblem: "+name+"...");
				data_out.writeChars(name+"\t");

				int percent=0,kn=0;
				if(str.contains("%")){
					str=str.replaceAll("%", "");
					percent=Integer.parseInt(str);	
					kn=problem.getCaseCount()*percent/100;
				}
				else
					kn = Integer.parseInt(str);

				System.out.println(" KNN="+kn);
				/*1*/	Kind kind = new KNNKind(kn);
				kind.setKind(d);// 0 is for Euclidean
				/*2*/	tree_classifier = Classification.classifier(ModelType.KNN, kind); // 4 is for KNN in modelType

				//	System.out.println("Learn");
				/*3*/	model = tree_classifier.learn(problem);

				stopwatch.stop();
				System.out.println(stopwatch.interval());
				stopwatch.start();
				//		    System.out.println("Evaluate");
				/*4*/	int res = tree_classifier.evaluate();
				double p = (double)res*100/problem.getTestData().length;
				DecimalFormat fmt = new DecimalFormat("#.##");
				System.out.printf("\n error=%d\n total= %d\n",res,problem.getTestData().length);
				//			System.out.printf("%.2f%%",p);
				System.out.print("validation= "+fmt.format(100-p)+"\t");
				//			data_out.writeDouble(100.00-p);
				data_out.writeBytes((fmt.format(100-p))+"\n");
				//			data_out.writeChars(Double.toString(100.00-p)+"\n");


			}


			stopwatch.stop();
			System.out.println(stopwatch.interval());
		}
		else{
			File file = new File("out/"+"Bayesresult.xls");
			// Create an output stream to the file.
			file_output = new FileOutputStream (file);
			// Wrap the FileOutputStream with a DataOutputStream
			data_out = new DataOutputStream (file_output);

			for (String name: file_paths){
				Problem problem = new Problem(PATH+"//"+name+"//" + name);
				System.out.println("\nProblem: "+name+"...");
				data_out.writeChars(name+"\t");

				/*1*/	Kind kind = new NaiveKind();
				/*2*/	tree_classifier = Classification.classifier(ModelType.BAYES, kind); // 2 is for Bayes in modelType

				//		System.out.println("Learn");
				/*3*/	model = tree_classifier.learn(problem);

				//			    System.out.println("Evaluate");
				/*4*/	int res = tree_classifier.evaluate();
				double p = (double)res*100/problem.getTestData().length;
				DecimalFormat fmt = new DecimalFormat("#.##");
				System.out.printf("\n error=%d\n total= %d\n",res,problem.getTestData().length);
				//				System.out.printf("%.2f%%",p);
				System.out.print("validation= "+fmt.format(100-p)+"\t");
				//				data_out.writeDouble(100.00-p);
				data_out.writeBytes((fmt.format(100-p))+"\n");
				//				data_out.writeChars(Double.toString(100.00-p)+"\n");

			}
		}
		scan.close();
		// Close file when finished with it..
		file_output.close ();
	}
}
