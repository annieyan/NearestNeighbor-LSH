package data;

import java.util.HashMap;
import java.util.Random;

public class RandomData {

	/** Creates a random dataset with points independently uniform on [-.5, .5] in each dimension
	 * @param n - the number of points to create
	 * @param D - the number of dimensions to use
	 * @return
	 */
	public static HashMap<Integer, Double[]> RandomDataset( Integer n, Integer D) {
		HashMap<Integer, Double[]> dataset = new HashMap<Integer, Double[]>();
		Random rnd = new Random();
		for (int obj = 0; obj < n; obj++) {
			Double[] key = new Double[D];
			for (int i = 0; i < D; i++) {
				key[i] = rnd.nextDouble() - .5;
			}
			dataset.put(obj+1, key);
		}
		return dataset;
	}

}
