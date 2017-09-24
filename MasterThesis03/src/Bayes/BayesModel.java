//package Bayes;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//
//import Parameter.BayesParam;
//import metadata.*;
//import kind.BayesKind;
//import kind.Kind;
//
//
///**
// * 
// * @author akamsi
// *
// */
//
//public class BayesModel implements ClassificationModel {
//
//	AttributeBayes [] attributes;
//	double[]classificationProbability;
//	String[] classValues;
//	BayesKind kind;
//	private static double NULL_VALUE;
//	ProblemInformation information;
//	//float[] classDistrib;
//	
//	public BayesModel(AttributeBayes[] attr, double[]classprob, Kind _kind, ProblemInformation prob_info) {
//		attributes = attr;
//		classificationProbability = classprob;
//		kind = (BayesKind) _kind;
//		information = prob_info;
//		if(kind.getKind() == BayesKind.EPSILON_BAYES){
//			NULL_VALUE = ((BayesParam)kind.getParameter()).EPSILON;
//		}
//		else if (kind.getKind() == BayesKind.SAMPLE_BAYES)
//		{
//			NULL_VALUE = ((BayesParam)kind.getParameter()).CLASS_PROBABILITY;
//		}
//	
//		
//	}
//	
//
//
//	@Override
//	public ClassificationModel join(ClassificationModel _model, boolean new_model) {
//		// return new model
//		
//			BayesModel model = (BayesModel) this;
//			BayesModel resultModel = (BayesModel) _model; 
//			
//			AttributeBayes[] attributes = resultModel.attributes;
//			AttributeBayes[] attributes1 = model.attributes;
//			
//			
//			//** compute new class distribution, NB!! changes first classifier!!!
//			for (int i=0;i<resultModel.classificationProbability.length;i++)
//			{
//				resultModel.classificationProbability[i] += model.classificationProbability[i];
//			}
//			
//			//** compute new attribute values distribution over class, NB!! changes the first classifier!!
//			
//			for(int i=0; i< attributes.length; i++)
//			{
//				if(attributes[i] instanceof DiscreteAttributeBayes)
//				{
//					DiscreteAttributeBayes attr = (DiscreteAttributeBayes) attributes[i];
//					DiscreteAttributeBayes attr1 = (DiscreteAttributeBayes) attributes1[i];
//					
//					int attributeValuesNumber = attr.attrIndexValues.size();
//					for(int classIndex=0; i<classValues.length;i++)
//						for(int attrIndex=0;attrIndex<attributeValuesNumber;attrIndex++){
//							attr.numberAttributes[classIndex][attrIndex]=attr1.numberAttributes[classIndex][attrIndex];
//							attributes[i] = attr;
//						}
//					
//				}
//				else
//				{
//					ContinuosAttributeBayes attr = (ContinuosAttributeBayes) attributes[i];
//					ContinuosAttributeBayes attr1 = (ContinuosAttributeBayes) attributes1[i];
//					double tempMean;
//					// ** computation of new mean and variance, NB!! changes the first classifier
//					
//					for (int classIndex=0; classIndex<this.classificationProbability.length;classIndex++)
//					{
//						//mean
//						tempMean = attr.mean[classIndex];
//						attr.mean[classIndex]=(attr.mean[classIndex]*resultModel.classificationProbability[classIndex]+
//							attr1.mean[classIndex]*model.classificationProbability[classIndex])
//							/(this.classificationProbability[classIndex]+model.classificationProbability[classIndex]);
//						
//						//variance
//						attr.variance[classIndex] = (attr.variance[classIndex]*(resultModel.classificationProbability[classIndex]-1)
//							+attr1.variance[classIndex]*(model.classificationProbability[classIndex]-1)+
//							this.classificationProbability[classIndex]*(tempMean - attr.mean[classIndex])+
//							model.classificationProbability[classIndex]*(attr1.mean[classIndex]-attr.mean[classIndex]))/
//							(model.classificationProbability[classIndex]+this.classificationProbability[classIndex]-1);
//						attributes[i] = attr;
//						
//					}
//					
//				}
//			}
//			
//			
//	
//			
//		
//		
//			if(new_model){
//				resultModel.attributes = attributes;
//				return resultModel;
//			}
//			else{
//				this.attributes = attributes;
//				return this;
//			
//			}	
//		
//		
//		
//		
//		
//	}
//
//	
//	private void save_model() throws IOException {
//		FileOutputStream f_out = new 
//		FileOutputStream("bayes.model");
//
//		// Write object with ObjectOutputStream
//		ObjectOutputStream obj_out = new
//		ObjectOutputStream (f_out);
//
//		// Write object out to disk
//		obj_out.writeObject (this);
//		
//	}
//
//	@Override
//	public long model_size() throws IOException {
//		save_model();
//		File file = new File("bayes.model");
//
//		// Get the number of bytes in the file
//		long length = file.length();
//		return length;
//		
//
//	
//	}
//
////	@Override
////	public String classify(String[] row, metadata.ProblemInformation _info,
////			int testIndx) {
////		// TODO Auto-generated method stub
////		return null;
////	}
////	
//	@Override
//	public String classify(String[] testData, metadata.ProblemInformation _info, int testIndx) {
//		
//		classValues = information.getClassValues();
//		int classIndex = information.getClassIndex();
//		double[] probability = new double[classValues.length]; //classificationProbability;
//
//		int dataLength = information.getTrainDataCount();//dataSet.getTrainData().length; ???
//		
//		for(int i=0;i<probability.length;i++)
//			probability[i]=classificationProbability[i]/dataLength;
//		
//		
//		for (int i=0;i<testData.length;i++)
//		{ if(i!=classIndex && !testData[i].equals("?"))
//			{
//				if(attributes[i] instanceof DiscreteAttributeBayes)
//				{
//					DiscreteAttributeBayes dab = (DiscreteAttributeBayes) attributes[i];
//					for (int j =0; j<probability.length;j++)
//					{	
//						int attrIndex = dab.getAttributeValueIndex(testData[i]);
//						/*if(classificationProbability[j]!=0 && dab.getProbabulAttrValue(j, attrIndex)!=0)
//							probability[j]*=(dab.getProbabulAttrValue(j, attrIndex)/(classificationProbability[j]));
//						else probability[j]*=NULL_VALUE;
//						*/
//						if(classificationProbability[j]==0&& dab.getProbabulAttrValue(j, attrIndex)!=0)
//							probability[j]*=(dab.getProbabulAttrValue(j, attrIndex)/NULL_VALUE);
//						else if(classificationProbability[j]!=0&& dab.getProbabulAttrValue(j, attrIndex)==0)
//							probability[j]*=(NULL_VALUE*(1/dab.getNominalValuesCount())/(classificationProbability[j]));
//						else if (classificationProbability[j]!=0 && dab.getProbabulAttrValue(j, attrIndex)!=0)
//							probability[j]*=(dab.getProbabulAttrValue(j, attrIndex)/(classificationProbability[j]));
//						else 
//							probability[j]*= (NULL_VALUE*(1/dab.getNominalValuesCount())/(NULL_VALUE));
//					}
//				}
//				
//				if(attributes[i] instanceof ContinuosAttributeBayes)
//				{
//					ContinuosAttributeBayes cab = (ContinuosAttributeBayes)attributes[i];
//					//gausee calculation
//					double gausse = 0;
//					
//					for (int j=0; j<probability.length;j++)
//					{
//						
//						
//							gausse = (cab.variance[j]==0||testData[i].equals("-"))?NULL_VALUE:1/Math.sqrt(2*Math.PI*cab.variance[j])*
//									Math.exp(
//											-0.5*(
//													Math.pow(
//															(Double.parseDouble(testData[i])-cab.mean[j]),2
//															)
//															/cab.variance[j]));
//							probability[j]*=gausse;
//						}
//					
//					
//					
//				}
//			}
//			
//		}
//		
//		
//	
//		double max = -1.0d;
//	    int maxIndex = -1;
//		for(int i = 0; i < probability.length; i ++) {
//			if(probability[i] > max) {
//				maxIndex = i;
//				max = probability[i];
//			}
//		}
//		
//		return information.getClassValues()[maxIndex];
//		
//		
//		
//		
//	}
//
//	
//}
