/**
 * @(#)ContinuousAttribute.java        1.5.0 09/01/18
 */
package metadata;

/**
 * A continuous attribute wrapping its continuous attribute values.
 */
public class ContinuousAttribute extends Attribute {
	/**
	 * Initialize a continuous attribute
	 * @param name the name of the attribute
	 * @param data the attribute values on the attribute
	 */
	float cut;
	public ContinuousAttribute(String name,String[] data) {
		super(name, data);
		//this.nominalValues = nominalValues;
	}
	
	/**
	 * The String exhibition of the continuous attribute
	 * @return the String exhibition of the discrete attribute
 	 * @see ml.dataset.DiscreteAttribute#toString()
	 */
	public String toString() {
		return name + " : continuous";
	}
	
	
	public void setCut(float _cut){
		cut = _cut;
	}

/*	*//**
	 * Get the nominal values of the attribute
	 *//*
	public String[] getNominalValues(){
		return this.nominalValues;
	}

	*//**
	 * Set the nominal values of the attribute
	 *//*
	public void setNominalValues(String[] nominalValues){
		this.nominalValues = nominalValues;
	}

	*//**
	 * Get the number of nominal values of the attribute
	 *//*
	public int getNominalValuesCount(){
		return nominalValues.length;
	}*/
}