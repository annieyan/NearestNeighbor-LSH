package methods;

import java.util.Random;

public class Helper {

	/**Creates a set of projection vectors in the given dimensions
	 * @param D - the dimension of the data
	 * @param m - the number of vectors to create
	 * @return
	 */
	public static double[][] CreateProjectionVectors( Integer D, Integer m) {
		double[][] projVectors = new double[m][D];
		Random rnd = new Random();
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < D; j++) {
				projVectors[i][j] = rnd.nextGaussian();
			}
		}
		return projVectors;
	}
	
}
