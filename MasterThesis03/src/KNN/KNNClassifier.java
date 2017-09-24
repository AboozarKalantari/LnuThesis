package KNN;

import kind.*;
import metadata.*;

/**
 * @author Aboozar
 */
public class KNNClassifier extends Classifier{
	KNNKind kind;
	
	public KNNClassifier(Kind kind) {
		this.kind = (KNNKind) kind;
	}
	
	@Override
	protected ClassificationModel _learn(Problem problem) {

		this.problem = problem;
		this.model = new KNNModel(problem, kind);
		
		return this.model;
	}
}
