package metadata;

public class ProblemInformation implements java.io.Serializable {

	private int trainDataLength;
	private int testDataLength;
	private String[] classValues;
	private int classIndex;
	private String[] attributeNames;
	private boolean[] isAttributeContinuous;
	
	public ProblemInformation(int _trainDataLength,int _testDataLength, 
			String[] _classValues, int _classIndex, String[] _attributeNames, boolean[] contattribute) {
		trainDataLength = _trainDataLength;
		testDataLength = _testDataLength;
		classValues = _classValues;
		classIndex = _classIndex;
		attributeNames = _attributeNames;
		isAttributeContinuous = contattribute;
	}
	
	public int getClassCount (){
		return classValues.length;
	}
	
	public String[] getClassValues (){
		return classValues;
	}
	
	public int getTestDataCount(){
		return testDataLength;
	}
	
	public int getTrainDataCount(){
		return trainDataLength;
	}
	
	public int getClassIndex(){
		return classIndex;
	}
	
	public String[] getAttributeNames()
	{
		return attributeNames;
	}
	
	public int getAttributesCount(){
		return attributeNames.length;
	}

	public boolean isAttributeContinuous(int j)
	{
		return isAttributeContinuous[j];
	}
}
