/**
 * @(#)MetaData.java        1.5.0 09/01/18
 */
package metadata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

/**
 * The meta data of a data set
 *
 */
public class MetaData {
	// All the attribute names
	private String[] attributeNames;
	// Whether the attributes are continuous attribute or discrete attribute
	private boolean[] isAttributesContinuous;
	/* The nominal attribute values. If the attribute is discrete, the corresponding
	 * item records its nominal values; otherwise, the corresponding item is null.
	 */
	private String[][] attributeNominalValues;
	private int classAttributeIndex;
	private ArrayList<LinkedHashSet<String>> attributeValues = new ArrayList<LinkedHashSet<String>>();;
	
	/**
	 * Initialize a MetaData.
	 * @param names the names of the attributes
	 * @param isContinuous whether the attributes are continuous attribute or not
	 * @param nominalValues the nominal values of the attributes. For continuous attributes,
	 *        their nominal values are set <i>null</i>.
	 */
	public MetaData(String[] names, boolean[] isContinuous, String[][] nominalValues){
		this.attributeNames = names;
		this.isAttributesContinuous = isContinuous;
		this.attributeNominalValues = nominalValues;
		setAttrValues();
	}

	private void setAttrValues() {
		
		for (int i =0; i<attributeNominalValues.length;i++)
		{ 	LinkedHashSet<String> values = new LinkedHashSet<String>();
			if (attributeNominalValues[i]!=null)
				for (int j = 0; j<attributeNominalValues[i].length;j++)
					values.add(attributeNominalValues[i][j]);
			attributeValues.add(values);
		}
	}

	
	public void setUpdateValues(Iterator<Integer> iterCont)
	{
		while(iterCont.hasNext())
		{
			int index = iterCont.next();
			String[] a = new String [attributeValues.get(index).size()];
			setAttributeNominalValuesAt(index, (String[]) attributeValues.get(index).toArray(a));
		}
	}
	
	
	/**
	 * Get the names of the attributes
	 */
	public String[] getAttributeNames(){
		return this.attributeNames;
	}

	/**
	 * Get the name of the index<sup>th</sup> attribute
	 */
	public String getAttributeNameAt(int index){
		return this.attributeNames[index];
	}

	/**
	 * @author akamsi
	 * Get array identification
	 */
	
	public ArrayList<Integer> getContiniousAttributes()
	{
		ArrayList<Integer> a = new ArrayList<Integer> ();
		for (int i = 0;i<isAttributesContinuous.length;i++)
			if(isAttributesContinuous[i])
				a.add(i);
		return a;
	}
	
	public LinkedHashSet<String> getAttrValues(int index)
	{
		return attributeValues.get(index);
	}
	
	public void addAttributeValueAt (int index, String value)
	{
		attributeValues.get(index).add(value);
	}
	
	
	/**
	 * Set the names of the attributes
	 */
	public void setAttributeNames(String[] names) {
		this.attributeNames = names;
	}

	/**
	 * Set the name of the index<sup>th</sup> attribute
	 */
	public void setAttributeNameAt(int index, String name) {
		this.attributeNames[index] = name;
	}

	
	public void setClassAttributeIndex(int index)
	{
		classAttributeIndex = index;
	}
	
	/**
	 * Query whether the attributes are continuous attribute or not
	 */
	public boolean[] isAttributesContinuous() {
		return this.isAttributesContinuous;
	}

	/**
	 * Query whether the index<sup>th</sup> attribute is continuous attribute or or
	 */
	public boolean isAttributeContinuousAt(int index) {
		return this.isAttributesContinuous[index];
	}

	public boolean[] getAttribiuteContiniuous(){
		return this.isAttributesContinuous;
	}
	
	/**
	 * Set whether the attributes are continuous attribute or not
	 */
	public void setAttributesContinuous(boolean[] isContinuous) {
		this.isAttributesContinuous = isContinuous;
	}

	/**
	 * Set whether the index<sup>th</sup> attribute is continuous attribute or not
	 */
	public void setAttributeContinuousAt(int index, boolean isContinuous) {
		this.isAttributesContinuous[index] = isContinuous;
	}

	/**
	 * Get the nominal values of the attributes
	 */
	public String[][] getAttributeNominalValues(){
		return this.attributeNominalValues;
	}

	/**
	 * Get the nominal values of the index<sup>th</sup> attribute
	 */
	public String[] getAttributeNominalValuesAt(int index){
		//if(this.attributeNominalValues.length>(index+1))
		return this.attributeNominalValues[index];
		//else return null;
	}

	/**
	 * Set the nominal values of the attributes
	 */
	public void setAttributeNominalValues(String[][] nominalValues){
		this.attributeNominalValues = nominalValues;
	}

	/**
	 * Set the nominal values of the index<sup>th</sup> attribute
	 */
	public void setAttributeNominalValuesAt(int index, String[] nominalValues){
		this.attributeNominalValues[index] = nominalValues;
	}

	/**
	 * Get the number of attributes in the meta data
	 */
	public int getAttributeCount(){
		return this.attributeNames.length;
	}

	public int getClassAttributeIndex()
	{
		return classAttributeIndex;
		
	}
	
	/**
	 * The String exhibition of the meta data
	 */
	public String toString(){
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < attributeNames.length; i ++) {
			sb.append(attributeNames[i]).append(" : ");
			if(isAttributesContinuous[i]){
				sb.append("Continuous");
			}
			else{
				sb.append(java.util.Arrays.toString(attributeNominalValues[i]));
			}
		}
		return sb.toString();
	}
	
	
}