package kind;

import Parameter.Parameter;

/**
 * 
 * @author akamsi
 * identifies the type of classifier, e.g. different algorithm to use
 *
 */


public interface Kind {

	public Parameter getParameter(); 
	
	/*sets the parameter needed for classification.
	 * Parameter is responsible for the data the learning algorithm requires*/
	
	public void setParameter(Parameter param);
	
	public void setKind(int kind);
	
	public int getKind();
}
