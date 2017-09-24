package Naive;

import java.io.IOException;

import kind.Kind;
import metadata.ClassificationModel;
import metadata.Problem;
import metadata.ProblemInformation;

public class NaiveModel implements ClassificationModel {

	public NaiveModel(Problem problem, Kind kind) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String classify(String[] row, ProblemInformation _info, int testIndx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClassificationModel join(ClassificationModel model, boolean new_model) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long model_size() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

}
