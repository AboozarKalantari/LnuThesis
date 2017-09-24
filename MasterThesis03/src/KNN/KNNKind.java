package KNN;

import kind.Kind;
import Parameter.Parameter;

public class KNNKind implements Kind{
	int euclidean = 0;
	int manhattan = 1;
	int overlapping =2;
	int probability =3;
	int dd;//Discrete distance
	int cd;//continuous distance
	int k;
	int dis;//distance method
	
	public KNNKind(int kN) {
		k = kN;
	}

	@Override
	public Parameter getParameter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setParameter(Parameter param) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setKind(int kind) {
			dis = kind;
	}

	@Override
	public int getKind() {
		return dis;
	}
	
	public void setNum(int n) {
		k = n;
	}

	public int getNum() {
		return k;
	}

}
