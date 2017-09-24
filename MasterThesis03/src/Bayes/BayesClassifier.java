//package Bayes;
//
//import java.util.Arrays;
//import java.util.HashMap;
//
//import Parameter.BayesParam;
//
////import Bayes.*;
//import metadata.*;
//import kind.BayesKind;
//import kind.Kind;
//
//public class BayesClassifier extends Classifier{
//
//	Kind kind;
//	BayesParam param = new BayesParam();
//	HashMap<String, Integer> classIndexValues = new HashMap<String, Integer>();
//	String[] classValues;
//	double classProbabilities[];
//	double [][] mean;
//	
//	public BayesClassifier(Kind k) {
//		kind = (BayesKind) k;
//		//param = (BayesParam) kind.getParameter();
//		
//	}
//	
//	
//	@Override
//	public ClassificationModel _learn(Problem _problem) {
//		// Filling preliminary data
//		problem = _problem;
//		classValues = problem.getClassValues();
//		if(kind.getKind() == BayesKind.SAMPLE_BAYES){
//				param.CLASS_PROBABILITY = 1.0/classValues.length;
//				kind.setParameter(param);
//		}
//		mean = new double[problem.getAttributeCount()][classValues.length];
//		for(int i=0; i<problem.getClassCount();i++)
//			classIndexValues.put(classValues[i], i);
//		classProbabilities = new double[classValues.length];
//		
//		//learning
//		learn();
//		
//		return model;
//	}
//	
//	
//	
//private void learn(){
//		
//		Attribute[] attributes = problem.getAttributes();
//		int classIndex = problem.getClassAttributeIndex();
//		DiscreteAttribute classAttribute = (DiscreteAttribute) attributes[classIndex];
//		String[] classValues = problem.getClassValues();
//		AttributeBayes [] attributesBayes = new AttributeBayes[attributes.length];
//		
//		
//		String[][] data =problem.getTrainData();
//		int dataLength = data.length;
//		Arrays.fill(classProbabilities, 0);
//		//** calculation of classification probability
//		String[] classData = classAttribute.getData();
//		for (int i =0; i<classData.length; i++)
//			classProbabilities[getClassValueIndex(classData[i])] +=1;
//		//**
//		
//		
//		for(int attrIndex =0; attrIndex<attributes.length; attrIndex++)
//		{
//			if(attrIndex!=classIndex)
//			{
//				if(attributes[attrIndex] instanceof DiscreteAttribute)
//				{
//					DiscreteAttribute attr = (DiscreteAttribute) attributes[attrIndex];
//					attributesBayes[attrIndex] = new DiscreteAttributeBayes(attr,classValues.length);
//					DiscreteAttributeBayes dab =(DiscreteAttributeBayes) attributesBayes[attrIndex];
//					String[] rowData = attr.getData();
//					for(int j=0; j<dataLength; j++)
//					{
//						if(!rowData[j].equals("?")) //??
//						{
//							int classValueIndex = getClassValueIndex(classAttribute.getData()[j]);
//							int attrValueIndex = dab.getAttributeValueIndex(rowData[j]);
//							dab.numberAttributes[classValueIndex][attrValueIndex]+=1;
//						}
//					}
//				
//				}	
//				
//				if(attributes[attrIndex] instanceof ContinuousAttribute)
//				{
//					ContinuousAttribute attr = (ContinuousAttribute) attributes[attrIndex];
//					
//					attributesBayes[attrIndex] = new ContinuosAttributeBayes(classValues);
//					ContinuosAttributeBayes cab = (ContinuosAttributeBayes) attributesBayes[attrIndex];
//					String[] rowData = attr.getData();
//					
//					for(int j =0; j<dataLength; j++)
//					{
//						if(!rowData[j].equals("-")) // was "?"
//						{
//							int classValueIndex = getClassValueIndex(classAttribute.getData()[j]);
//							cab.mean[classValueIndex]+=Double.valueOf(rowData[j]);
//						}
//					}
//					//mean calculation
//					for (int i=0;i<classValues.length;i++){
//						
//						cab.mean[i]/=classProbabilities[i];
//						mean[attrIndex][i]=cab.mean[i];
//					}
//						
//					//variance calculation
//					for(int j=0; j<dataLength;j++)
//					{
//						if(!rowData[j].equals("-")) // was "?"
//						{
//							int classValueIndex = getClassValueIndex(classAttribute.getData()[j]);
//							cab.variance[classValueIndex]+= Math.pow((Double.valueOf(rowData[j])-cab.mean[classValueIndex]),2.0);
//						}
//					}
//					
//					for (int i=0;i<classValues.length;i++)
//						if(classProbabilities[i]!=0) //TODO: check if the class occurs 0 times in the sample
//							cab.variance[i]/=(classProbabilities[i]-1);
//						else
//							cab.variance[i]/=(classProbabilities[i]);
//					
//										
//					
//				}
//					
//				
//			}
//			
//			
//		}
//		
//		
//		model = new BayesModel(attributesBayes,classProbabilities, kind, prob_info);
//		
//		
//
//}
//
//private int getClassValueIndex(String value){
//	return classIndexValues.get(value);
//}
//
//
//}
