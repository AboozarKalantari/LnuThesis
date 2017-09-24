package metadata;

import metadata.*;
/**
 *@author akamsi
 *
 */

public abstract class Classifier {

	
	protected Problem problem;
	protected ClassificationModel model;
	private boolean isLeaned = false;
	protected ProblemInformation prob_info;
	
/*	public Classifier(int type, Kind kind) {
		this.kind = kind;
		
	}*/
	
	
	/**Evaluates the current classifier on the test data provided in Problem **/
	public int evaluate()
	{
		if(isLeaned){
			
			return getTestError();
		}
		else{
			System.out.println("The model is not learned. Learn the model first");
			return -1;
		}
			
	}
	
		
	public int getTestError(){
		String[] classificationResults =null;
		//Stopwatch.start();
		classificationResults = _classify(problem.getTestData());
		//Stopwatch.stop();
		
		int testError = 0;
		int classAttributeIndex = problem.getClassAttributeIndex();
		for(int i = 0; i < classificationResults.length; i ++)
			if(!classificationResults[i].equals(problem.getTestData()[i][classAttributeIndex]))
				testError ++;
		return testError;
	}
	
	private  String[] _classify(String[][] testData) {
		if(isLeaned){
			String[] results = new String[testData.length];
			
			for(int testIndex = 0; testIndex < testData.length; testIndex ++) {
				results[testIndex] = model.classify(testData[testIndex], prob_info,testIndex);
			}
			
			return results;
		}
			
		else{
			System.out.println("The model is not learned. Learn the model first");
			return null;
		}
		
	}
	
	
	/**learns the current classifier on the given problem and returns Classification model */
	
	//public
		
	public ClassificationModel learn(Problem problem)
	{
		isLeaned = true;
		prob_info = new ProblemInformation(problem.getTrainData().length, problem.getTestData().length,
				problem.getClassValues(), problem.getClassAttributeIndex(), problem.getMetaData().getAttributeNames(), problem.getMetaData().getAttribiuteContiniuous());
		return _learn(problem);
		
	}
	
	protected abstract ClassificationModel _learn (Problem problem);
	
	/** classifiers the data, changes data by adding the class label */
	public void classsify (String[][] classificationData)
	{
		String[] results = _classify(classificationData);
		int classIndex = problem.getClassAttributeIndex();
		
		for (int i =0; i<classificationData.length;i++)
			classificationData[i][classIndex] = results[i];
			
	}
	
	
	
	public ClassificationModel getClassificationModel()
	{
		return model;
	}
	
	
}
