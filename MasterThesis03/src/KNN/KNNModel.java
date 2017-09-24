package KNN;

import java.io.IOException;
import java.util.*;

import util.*;
import metadata.*;

/**
 * @author Aboozar
 */
@SuppressWarnings("serial")
public class KNNModel implements ClassificationModel {

	String [][] stanData,stanTest;
	String [][] test,train;
	double[] maxTest,minTest;
	double[][][] classCount;//row is attribute values, column is attribute number, 3rd is counts in classes
	double[][] totalCount;
	List<Map<String, Double> > totalCountMap;
	List<Map<String, Double[]> > classCountMap;
	double[] std,mean;

	//	private boolean n=false,s=false;
	//	
	//	public boolean isN() {
	//		return n;
	//	}
	//
	//	public void setN(boolean n) {
	//		this.n = n;
	//	}
	//
	//	public boolean isS() {
	//		return s;
	//	}
	//
	//	public void setS(boolean s) {
	//		this.s = s;
	//	}
	Problem problem;
	KNNKind kind;
	MetaData md;
	Stopwatch stopwatch = new Stopwatch();
	ArrayList<Train> trains = new ArrayList<Train>();
	boolean isSorted=false;
	/**
	 * @param trainData
	 * @param testData
	 */
	public KNNModel(Problem problem ,KNNKind k) {
		super();
		this.problem = problem;
		kind = k;
		md = problem.getMetaData();
		maxTest = new double[problem.getTestData()[0].length];
		minTest = new double[problem.getTestData()[0].length];
		train = problem.getTrainData();
		test = problem.getTestData();
		isSorted=false;

		//		totalCountMap = new

		findMaxMin();
		findNum();
		standard();
		//		stanData = standard(train);
		//		stanTest = standard(test);
	}

	@Override
	public String classify(String[] row, ProblemInformation _info,int testIndx) {

		//		maxValues();

//System.out.println("Distance");
		ArrayList<classAttr> sumList;
		Train t;
		int l=problem.getCaseCount();
		//		ArrayList<Train> trains = new ArrayList<Train>();
		if(trains.isEmpty()){
			
			for(int j=0;j<l;j++){//get Trains for each test (trainData.length)
				t = new Train();
				t.trainLine = train[j]; //trainData[j]
				trains.add(t);
				t.weight=1/(distance(row, t.trainLine)+ 0.001);	
			}
		}
		else
			for(int j=0;j<l;j++){//get Trains for each test (trainData.length)
				t = trains.get(j);
				t.weight=1/(distance(row, t.trainLine)+ 0.001);	
			}
		//			System.out.print(" "+j+" "+ problem.getCaseCount());

//		System.out.print("Sort");
		TrainComparator com = new TrainComparator();
		Collections.sort(trains, com);
		isSorted=true;
//		sort(trains);//this method is too slow 

//		System.out.println("CreateSumList");
		sumList = new ArrayList<classAttr>();
		createSum(sumList); // Create empty sum list that contain the list of classes with class name(attribute) and sum

		for(int k=0;k<kind.getNum();k++){// get the k top neighbor
			int c = problem.getClassAttributeIndex();//Index of class attribute in data
			String a=trains.get(k).trainLine[c]; // The name of the k'th data class name
			double b = trains.get(k).weight;	// The weight of that class
			for(int i1=0;i1<sumList.size();i1++){
				if(a.equals(sumList.get(i1).name)){// find the class object in the sumlist and
					sumList.get(i1).sum+=b;		// add the weight to sum 
					break;
				}
			}
		}
		//calculate the class and choose the right class(y[k])
		double max = 0.0;
		int indx=0;
		for(int k=0;k<sumList.size();k++){
			if(sumList.get(k).sum>max){
				max = sumList.get(k).sum;
				indx=k;
			}
		}
		System.out.printf("%d / %d: test=%s\n ",testIndx+1,test.length,sumList.get(indx).name);
		return sumList.get(indx).name;
	}

	@Override
	public ClassificationModel join(ClassificationModel model, boolean new_model) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long model_size() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}


	void sort(ArrayList<Train> list){

		Train t = new Train();
		//sort weight
		for(int i1=0;i1<list.size();i1++)//foreach(trains)
			for(int i2=i1+1;i2<list.size();i2++)
				if(list.get(i1).weight<list.get(i2).weight){
					t = list.get(i1);
					list.set(i1, list.get(i2));
					list.set(i2, t);
				}
	}

	void createSum(ArrayList<classAttr> sumList){	
		String [] str = problem.getClassValues();
		for(int i=0;i<str.length;i++){
			classAttr cattr= new classAttr();
			cattr.name = str[i];
			sumList.add(cattr);
		}
	}
	private double distance(String[]x1, String[] x2){//X1(test data) X2(train data) NEED NORMALIZE 
		double d1=0.0, d2=0.0,norm1,norm2;
		//		MetaData md = problem.getMetaData();

		int m = problem.getClassAttributeIndex();
		for(int i=0;i<m;i++){//column
			if(x1[i].equals("-")||x2[i].equals("-"))
				continue;
			else{
				if(md.isAttributeContinuousAt(i)){
//					norm1 = Double.parseDouble(x1[i]);
//					norm2 = Double.parseDouble(x2[i]);

//					norm1 = standardize(Double.parseDouble(x1[i]),i);
//					norm2 = standardize(Double.parseDouble(x2[i]),i);

					norm1 = normalize(Double.parseDouble(x1[i]),i);
					norm2 = normalize(Double.parseDouble(x2[i]),i);
					
					if(kind.getKind()<2 )//0 = euclidean/over   1= eucl/prob
						d1+= euclidean(norm1 ,norm2);
					else
						d1+= manhattan(norm1 ,norm2);
				}

				else{

					if(kind.getKind()%2==0) //0,2: overlapping	1,3: probability
						d2 = d2 + overlapping(x1[i], x2[i]);
					else
						d2 += probability(x1[i], x2[i], i);

				}
			}

		}
		if(kind.getKind() < 2)//Euclidean
			return Math.sqrt(d1)+ d2;
		else
			return d1+d2;
	}

	private double euclidean(double x1, double x2){
		double d=0;

		d+= Math.pow((x1-x2), 2);
		return d;
	}

	private double manhattan(double x1, double x2){
		double d=0;

		d+= Math.abs(x1-x2);
		return d;
	}

	private double overlapping(Object x1, Object x2){//x1 & x2 are two attributes that we want fine distance between them
		if(x1.equals(x2)/*||(x1.equals("-"))||(x2.equals("-"))*/)
			return 0;
		else
			return 1;
	}

	private double probability(String x1, String x2, int col){//x1 & x2 are two attributes that we want fine distance between them and col = which column 
		int c = problem.getClassCount();//number of classes
		double sum = 0.0;
		if((x1.equals(x2))/*||(x1.equals("-"))||(x2.equals("-"))*/)
			return 0;
		else{
			double n1, n2, n3, n4;
			//			n3 = numOf(x1, col);
			n3 = totalCountMap.get(col).get(x1);
			//			n4 = numOf(x2, col);//totalCount[][col];
			n4 = totalCountMap.get(col).get(x2);
			for(int j=0 ; j<c ; j++){
				//				n1 = numOf(x1, col, problem.getClassValues()[j]);
				n1 = classCountMap.get(col).get(x1)[j];
				//				n2 = numOf(x2, col, problem.getClassValues()[j]);
				n2 = classCountMap.get(col).get(x2)[j];
				double p1 = n1/n3, p2 = n2/n4;
				sum += Math.abs(p1-p2);
			}
		}
		return sum;
	}

	//	private double numOf(String x1,int attr, String c){//Number of the x1 in 'attr' column as attribute and 'c' as class name
	//		if(x1.equals("-"))
	//			return 0;
	//		else{
	//		int classAttributeIndex = problem.getClassAttributeIndex();
	//		int count=0, num =0;
	//		for(int i = 0; i <train.length; i++)
	//			if(x1.equals(train[i][attr])){
	//				num++;
	//				if(c.equals(train[i][classAttributeIndex]))
	//					count++;
	//			}
	//		return count;
	//		}
	//	}

	private double numOf(String x1,int attr){//Number of the x1 in 'attr' column as attribute in all classes
		int count=0;
		for(int i = 0; i < train.length; i ++)
			if(x1.equals(train[i][attr]))
				count++;
		return count;
	}

	private double numOf(String x1,String[] list){//Number of the x1 in list
		int count=0;
		for(int i = 0; i < list.length; i ++)
			if(x1.equals(list[i]))
				count++;
		return count;
	}

	private double numOf(String x1, int attr, String c){//Number of the x1 in 'attr' column as attribute and 'c' as class name
		if(x1.equals("-"))
			return 0;
		else{
			int classAttributeIndex = problem.getClassAttributeIndex();
			int count=0, num =0;
			for(int i = 0; i <train.length; i++)
				if(x1.equals(train[i][attr])){
					//				num++;
					//				totalCount[row][attr]++;
					if(c.equals(train[i][classAttributeIndex]))
						count++;

				}
			//		totalCount[row][attr]=num;
			return count;
		}
	}
	private int maxValues(){

		//		Attribute[] attribs = problem.getAttributes();
		String[][] values = md.getAttributeNominalValues();
		int max = 0,n = values.length;
		for (int i = 0; i < n; i++) {// It is also count the class names column we should check whether it should be or not
			if(!md.isAttributeContinuousAt(i)){
				int m = values[i].length;
				if( m > max)
					max = values[i].length;
			}
		}

		return max;
	}
	public void findNum(){//this method is for initializing the probability information

		Attribute[] attribs = problem.getAttributes();//get attributes (columns) 
		String[] classvalues = md.getAttributeNominalValuesAt(attribs.length-1);//get class names
		totalCount = new double[maxValues()][attribs.length];
		classCount = new double[maxValues()][attribs.length][classvalues.length];

		totalCountMap = new ArrayList<Map<String,Double>>();
		classCountMap = new ArrayList<Map<String,Double[]>>();
		Map<String, Double> map1;
		Map<String, Double[]> map2;

		for (int i = 0; i < attribs.length-1; i++) {//number of attributes in train set it will use as row
			if(md.isAttributeContinuousAt(i)){		//it will not find for last column(class names Attributes) 
				totalCountMap.add(i, null);
				classCountMap.add(i, null);
				continue;
			}
			String[] values = md.getAttributeNominalValuesAt(i);//get values of i'th attribute

			map1 = new HashMap<String, Double>();
			map2 = new HashMap<String, Double[]>();
			for (int j = 0; j < values.length; j++) {//number of values in that(i'th) attribute it will use as column

				totalCount[j][i] = numOf(values[j], attribs[i].getData());

				map1.put(values[j], totalCount[j][i]);
				Double[] clsCount = new Double[classvalues.length];
				map2.put(values[j], clsCount);

				for (int k = 0; k < classvalues.length; k++) {
					classCount[j][i][k] = numOf(values[j], i, classvalues[k]);
					clsCount[k] = numOf(values[j], i, classvalues[k]);
				}
			}

			totalCountMap.add(i, map1);
			classCountMap.add(i, map2);

		}
		//		System.out.println("attr"+problem.getAttributeCount());
		//		for (int i = 0; i < train[0].length; i++) {//column
		////			numOf(x1, i);
		//			
		//		}
	}

	public double normalize(double x,int i){
		return (x-minTest[i])/(maxTest[i]-minTest[i]);
	}

	public void findMaxMin(){
		//
		//		String [][] list1 = problem.getTrainData();
		//		String [][] list2 = problem.getTestData();
		//		MetaData md = problem.getMetaData();
		int temp = 0;
		for(int j=0;j<train[0].length-1;j++){//column
			if(md.isAttributeContinuousAt(j)){
				if(train[temp][j].equals("-")){
					temp++;
					j--;
					continue;
				}
				else{
					maxTest[j] = Double.parseDouble(train[temp][j]);
					minTest[j] = Double.parseDouble(train[temp][j]);
					temp = 0;
					for(int i=0;i<train.length;i++){//row of train data
						if(train[i][j].equals("-"))
							continue;
						else{
							double n = Double.parseDouble(train[i][j]);
							if(n > maxTest[j])
								maxTest[j] = n;
							if(n < minTest[j])
								minTest[j] = n;
						}
					}
					for(int i=0;i<test.length;i++){//row of test data
						if(test[i][j].equals("-"))
							continue;
						else{
							double n = Double.parseDouble(test[i][j]);
							if(n > maxTest[j])
								maxTest[j] = n;
							if(n < minTest[j])
								minTest[j] = n;
						}
					}
				}
			}
		}
	}

	public double standardize(double x,int i){
		return (x-mean[i])/(std[i]);
	}

	public void standard(){//Standardize the train set
		//		String [][] out = list;

		int size = md.getAttributeCount();
		mean = new double[size];
		std = new double[size];
		for(int j=0;j<size;j++){//column
			if(md.isAttributeContinuousAt(j)){
				int count = 0;
				double sum=0; //mean=0, sd =0;
				for(int i=0;i<train.length;i++){//row
					if(!train[i][j].equals("-")){
						sum += Double.parseDouble(train[i][j]);
						count++;
					}

				}
				mean[j] = sum / count;
				for(int i=0;i<train.length;i++){
					if(!train[i][j].equals("-")){
						double diff = (Double.parseDouble(train[i][j])-mean[j]);
						std[j] += diff*diff;
					}
				}
				std[j] = Math.sqrt(std[j]/train.length);
				//			for(int i=0;i<list.length;i++)
				//				out[i][j] = Double.toString((Double.parseDouble(list[i][j])-mean)/(sd));
			}
		}
		//		return out;
	}

	public String [][] standard(String[][] list){
		String [][] out = list;

		for(int j=0;j<list[0].length;j++){//column
			double sum=0, mean=0, sd =0;
			for(int i=0;i<list.length;i++){//row
				sum += Double.parseDouble(list[i][j]);
			}
			mean = sum / list.length;
			for(int i=0;i<list.length;i++){
				double diff = (Double.parseDouble(list[i][j])-mean);
				sd += diff*diff;
			}
			sd = Math.sqrt(sd/list.length);
			for(int i=0;i<list.length;i++)
				out[i][j] = Double.toString((Double.parseDouble(list[i][j])-mean)/(sd));
		}
		return out;
	}
	//	static double StanDev(double[] values,int numAmount){
	//		double sum=0, mean=0, sq_diff_sum=0;
	//		for(int i = 0; i < numAmount; i++){
	//			sum += values[i];
	//		}
	//			mean = sum / numAmount;
	//			sq_diff_sum = 0;
	////		}           
	//		for(int i = 0; i <numAmount ; ++i){
	//			double diff = values[i] - mean;
	//			sq_diff_sum+= diff * diff;
	//		}         
	//		double deviance= sq_diff_sum /numAmount ;         
	//		return Math.sqrt(deviance);           
	//	} 


	public class Train{
		String[] trainLine;
		double weight;
	}
	public class classAttr{
		String name;
		double sum;
	}
	public class TrainComparator implements Comparator<Train> {

	    @Override
	    public int compare(Train t1, Train t2) {
	          double w1=t1.weight;
	          double w2=t2.weight;

	          if (w1 < w2 ){
	        	   return 1;
	        	  }
	        	  else if (w1 > w2){
	        	   return -1;
	        	  }
	        	  else
	        	   return 0;
	    }           
	}

}
