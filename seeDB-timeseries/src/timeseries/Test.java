package timeseries;

import java.util.Arrays;

import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

public class Test {
	public static void main (String[] args) {
		
		double[] kca = {227.0, 152.0, 236.0, 34.0, 183.0, 61.0, 181.0, 258.0, 110.0, 68.0, 385.0, 80.0, 193.0, 159.0, 186.0, 63.0, 92.0, 91.0, 113.0, 118.0, 157.0, 50.0, 105.0, 256.0};
		
		double[] candDataArray =  {256, 227, 258, 159, 118, 110, 63, 50, 34, 61, 80, 91, 68, 92, 105, 183, 181, 193, 113, 152, 186, 157, 236, 385.0}; //kca
		double[] targetDataArray = {17, 11, 8, 1, 6, 4, 5, 0, 1, 0, 10, 7, 15, 4, 2, 0, 1, 6, 4, 5, 11, 14, 8, 9}; // votelittlemixuk
		double[] cand2DataArray = {1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0}; // sportsroadhouse
		//double[] commx = {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 5};
		double[] commx = {0.0, 0.0, 2.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 5.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 2.0};
		
		double coeff3 = new PearsonsCorrelation().correlation(targetDataArray, candDataArray);
		double coeff4 = new PearsonsCorrelation().correlation(targetDataArray, cand2DataArray);
		
		double[] targetDataArrayNor = StatUtils.normalize(targetDataArray);
		double[] candDataArrayNor = StatUtils.normalize(candDataArray);
		double[] cand2DataArrayNor = StatUtils.normalize(cand2DataArray);
		
		double coeff1 = new PearsonsCorrelation().correlation(targetDataArrayNor, candDataArrayNor);
		double coeff2 = new PearsonsCorrelation().correlation(targetDataArrayNor, cand2DataArrayNor);
		
		double coeff = new PearsonsCorrelation().correlation(kca, commx);
		
		System.out.println(coeff);
		
		System.out.println(coeff1);
		System.out.println(coeff2);
		System.out.println(coeff3);
		System.out.println(coeff4);
		System.out.println(Arrays.toString(targetDataArrayNor));
		System.out.println(Arrays.toString(candDataArrayNor));
		System.out.println(Arrays.toString(cand2DataArrayNor));
	}
}