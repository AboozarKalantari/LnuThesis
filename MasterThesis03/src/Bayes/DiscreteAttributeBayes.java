package Bayes;

import java.util.Arrays;
import java.util.HashMap;

import metadata.DiscreteAttribute;



public class DiscreteAttributeBayes extends AttributeBayes {

	String name;
	HashMap<String,Integer> attrIndexValues = new HashMap<String, Integer>();
	float[][] numberAttributes;
	
	public DiscreteAttributeBayes(DiscreteAttribute attribute, int classValuesLenght) {
		name = attribute.getName();
		String[] values = attribute.getNominalValues();
		for(int i=0;i<attribute.getNominalValuesCount();i++)
			attrIndexValues.put(values[i],i);
		numberAttributes = new float[classValuesLenght][values.length];
		
		for(int i=0;i<classValuesLenght;i++)
			Arrays.fill(numberAttributes[i], 0);
	}
	
	
	
	public String getName()
	{
		return name;
	}
	
	public int getAttributeValueIndex(String value)
	{
		/*System.out.println(this.name);
		System.out.println(value);
		*/return attrIndexValues.get(value);
	}


	public float getProbabulAttrValue(int classIndex, int attrValueIndex){
		return numberAttributes[classIndex][attrValueIndex];
	}
	/*@Override
	public float[] getValue(int i) {
		return numberAttributes[i];
	}*/
	
	public double getNominalValuesCount()
	{
		return attrIndexValues.size();
	}
	
	
	
}
