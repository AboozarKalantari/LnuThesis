package Naive;

import KNN.KNNModel;
import kind.Kind;
import metadata.ClassificationModel;
import metadata.Classifier;
import metadata.Problem;

public class NaiveClassifier extends Classifier {

	Kind kind;
	public NaiveClassifier(Kind k) {
		this.kind=k;
	}

	@Override
	protected ClassificationModel _learn(Problem problem) {
		this.problem = problem;
		this.model = new NaiveModel(problem, kind);
		
		return this.model;
	}

}
