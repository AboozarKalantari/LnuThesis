/**
 * @(#)Attribute.java        1.5.0 09/01/18
 */
package metadata;

/**
 * An attribute wrapping its attribute values.
 */
public class Attribute implements java.io.Serializable{
	/**
	 * The name of the attribute
	 */
	protected String name;
	/**
	 * The attribute values
	 */
	protected String[] data;

	/**
	 * Initialize an attribute with the specified name and attribute values
	 */
	public Attribute(String name, String[] data){
		this.name = name;
		this.data = data;
	}

	/**
	 * Get the name of the attribute
	 */
	public String getName(){
		return this.name;
	}

	/**
	 * Set the name of the attribute
	 */
	public void setName(String name){
		this.name = name;
	}

	/**
	 * Get the attribute values on the attribute
	 */
	public String[] getData(){
		return this.data;
	}

	/**
	 * Set the attribute values on the attribute
	 */
	public void setData(String[] data) {
		this.data = data;
	}

	/**
	 * The String exhibition of the attribute.
	 */
	public String toString() {
		return name;
	}
}