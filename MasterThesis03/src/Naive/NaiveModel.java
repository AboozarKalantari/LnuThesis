package Naive;

import java.io.IOException;
import java.util.ArrayList;

import kind.Kind;
import metadata.*;
import util.Statistics;

public class NaiveModel implements ClassificationModel {
	
	Problem problem;
	MetaData md ;//Metadata of problem(there is some additional info about dat set like isCountinuse,...etc)
	int d;//number of tuples in training set
//	int[] classNumList;//number of tuples for each class
//	int[][] classAttrMat;// number of each attribute in each class in evaluate time
	String [][] test,train;
	metadata.Attribute[] attribs;// attributes of data set
	double mean,sd;
	Statistics statistics;
	Node[] classList;// list of classes in data set
	int cCount,attCount;
//	double[] pList;//list for Probability of each class "P(ci)"
	public NaiveModel(Problem problem, Kind kind) {
		
		this.problem=problem;
		md = problem.getMetaData();
//		statistics = new Statistics();
		
//		classNumList = new int[cCount];
//		classAttrMat = new int [cCount][attCount];
		
		initialize();
		classList = new Node[cCount];
		for (int i = 0; i < cCount; i++) {
			classList[i]=new Node();
			classList[i].value=problem.getClassValues()[i];
//			findStatistics(i);
		}
		for(int i=0;i<cCount;i++){
//			classList[i].num=0;
			
			for(int j=0;j<d;j++){
				String s1=train[j][attCount];
				String s2=classList[i].value;  //problem.getClassValues()[i];
				if(s1.equals(s2)){
					classList[i].tupleList.add(train[j]);
					classList[i].num++;
				}
						
			}
		}
		
//		pList = new double[cCount];
		for (int i = 0; i < cCount; i++) {
			classList[i].tuples=new String [classList[i].num][attCount];
			classList[i].p = (double)classList[i].num/d; 
			findStatistics(i);
		}
		
//		mean= util.Statistics.
	}

	@Override
	public String classify(String[] row, ProblemInformation _info, int testIndx) {
		
		for (int i = 0; i < row.length-1; i++) {
			if(md.isAttributeContinuousAt(i)){
				for(int j=0; j<cCount; j++){
					double s=classList[j].sd[i];
					double m=classList[j].mean[i];
					double x=Double.parseDouble(row[i])-m;
					classList[j].pAttrib[i]=(1/Math.sqrt(2*Math.PI)*s)*Math.pow(Math.E, -(Math.pow(x, 2))/2*Math.pow(s, 2));
				}
			}
			else{
				for(int j=0; j<cCount; j++){
					classList[j].pAttrib[i]=countNum(j,i,row[i])/classList[j].num;
				}
			}
			
		}
		
		for(int i=0; i<cCount; i++){
			for (int j = 0; j < attCount; j++) {
				classList[i].prob=classList[i].p*classList[i].pAttrib[j];
			}
		}
		
		double max = 0.0;
		int indx=0;
		for(int i=0;i<cCount;i++){
			if(classList[i].prob>max){
				max = classList[i].prob;
				indx=i;
			}
		}
		System.out.printf("%d / %d: test=%s\n ",testIndx+1,test.length,classList[indx].value);
				
		return classList[indx].value;
	}
	
/** this method used for counting the number of tuples in class ci
 *  that the ai index of attribute is equal xk
 */
	private int countNum(int ci, int ai, String xk) {//class index and attribute index xk=the test attribute
		
		int count=0;
		for (int i = 0; i < classList[ci].num; i++) {
			if(classList[ci].tupleList.get(i)[ai].equals(xk)){
				count++;
			}
		}
		return count;
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

	private void initialize(){
		cCount = problem.getClassCount();//classCount
		attCount = problem.getAttributeCount()-1;//AttributeCounte
		attribs = problem.getAttributes();
		d= problem.getCaseCount();
		train=problem.getTrainData();
		test= problem.getTestData();
		
	}
	
	private void findStatistics(int i) {
		
		for (int j = 0; j < attribs.length-1; j++) {
			if (md.isAttributeContinuousAt(j)) {
				float[] temp=new float[classList[i].num];
				for (int k = 0; k < temp.length; k++) {
					temp[k]=Float.parseFloat(classList[i].tupleList.get(k)[j]);
//					temp[k]=Float.parseFloat(attribs[j].getData()[k]);
				}
				if(temp.length>0){//this if for avoid the value of mean and sd become NaN=Not a Number
					classList[i].mean[j]=Statistics.mean(temp);
					classList[i].sd[j]=Statistics.standardDeviation(temp);
				}
				
			}
			
		}
		
		
	}

	private class Node{
		String value;
		int num;//Number of tuples in D with class ci
		double prob;//final probability for comparison(final probability(P(ci)*P(Xk|Ci)))
		double p;//probability:  P(ci) 
		double[] pAttrib;
		int[] attribNum;// number of each attribute in each class in evaluate time
		double[] mean;//mean of values for each attribute
		double[] sd; //standard deviation of values for each attribute
		String[][] tuples;//Part of train set with class Ci
		ArrayList<String[]> tupleList;
		
		public Node() {
			value="";
			num=0;
			prob=0;
			p=0;
			pAttrib = new double[attCount];//P(Xk|Ci)
			attribNum = new int[attCount];
			mean = new double[attCount];
			sd = new double[attCount];
//			tuples = new String[][]{};
			tupleList = new ArrayList<String[]>();
		}
		
		
	}


	
}
