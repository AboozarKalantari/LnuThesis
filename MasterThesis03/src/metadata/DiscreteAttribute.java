/**
 * @(#)DiscreteAttribute.java        1.5.0 09/01/18
 */

package metadata;

/**
 * A discrete attribute wrapping its discrete attribute values.
 */
public class DiscreteAttribute extends Attribute {
	// The nominal discrete attribute values of the attribute
	private String[] nominalValues;

	/**
	 * Initialize a discrete attribute. 
	 * @param name the name of the attribute
	 * @param nominalValues the nominal values of the attribute
	 * @param data the attribute values on the attribute
	 */
	public DiscreteAttribute(String name, String[] nominalValues, String[] data) {
		super(name, data);
		this.nominalValues = nominalValues;
	}

	/**
	 * Get the nominal values of the attribute
	 */
	public String[] getNominalValues(){
		return this.nominalValues;
	}

	/**
	 * Set the nominal values of the attribute
	 */
	public void setNominalValues(String[] nominalValues){
		this.nominalValues = nominalValues;
	}

	/**
	 * Get the number of nominal values of the attribute
	 */
	public int getNominalValuesCount(){
		return nominalValues.length;
	}

	/**
	 * The String exhibition of the discrete attribute
	 */
	public String toString() {
		return name + " : " + java.util.Arrays.toString(nominalValues);
	}
	
	@Override
	public int hashCode() {
		int hashCode = name.hashCode()+nominalValues.hashCode(); 
		return hashCode;
	}
	
	@Override
	public boolean equals(Object obj) {
		DiscreteAttribute atr = (DiscreteAttribute) obj;
		return (this.name.equals(atr.name)&& this.nominalValues.equals(atr.nominalValues));
	}
}