package Bayes;

import java.util.Arrays;

import javax.smartcardio.ATR;

public class ContinuosAttributeBayes extends AttributeBayes {

	double[] mean;
	double[] variance;
	
	public ContinuosAttributeBayes(String[] classValues) {
		mean = new double[classValues.length];
		variance = new double[classValues.length];
		Arrays.fill(mean, 0);
		Arrays.fill(variance, 0);
	}
	
	
	
}
