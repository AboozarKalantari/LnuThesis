package metadata;



import KNN.*;
import Naive.NaiveClassifier;
import kind.*;


public class Classification {

	
	public static Classifier classifier (int type, Kind kind)
	{
		if(type==ModelType.BAYES){
			return new NaiveClassifier(kind);
		}
		
		else if( type == ModelType.DTREE){
			//return new DTreeClassifier(kind);
		}
		else if(type == ModelType.DTERM){
			//return new DTermClassifier(kind);
		}
		else if(type == ModelType.SVM){
			//return new SvmClassifier(kind);
		}
		else if(type == ModelType.KNN){
			return new KNNClassifier(kind);
		}
		else if(type == ModelType.BAYESNETWORK){
			
		}
		return null;
	}
	
	
}
