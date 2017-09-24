package metadata;

import java.io.IOException;


public interface ClassificationModel extends  java.io.Serializable {

	public String classify (String row[], ProblemInformation _info,int testIndx);
	
	public ClassificationModel join (ClassificationModel model, boolean new_model);
	
	//public void save_model() throws IOException;
	
	public long model_size() throws IOException;
	
}
