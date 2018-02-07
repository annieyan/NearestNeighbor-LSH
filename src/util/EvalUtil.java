package util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

public class EvalUtil {

	private static Iterator<Integer> doc2_keys;

	public static double Distance( Double[] x1, Double[] x2 ) {
		double dist = 0.0;
		for (int i = 0; i < x1.length; i++) {
			dist += Math.pow(x1[i]-x2[i], 2);
		}
		return Math.sqrt(dist);
	}
	
	public static double Distance( double[] x1, double[] x2 ) {
		double dist = 0.0;
		for (int i = 0; i < x1.length; i++) {
			dist += Math.pow(x1[i]-x2[i], 2);
		}
		return Math.sqrt(dist);
	}
	

	/**
	 * Calculates the distance between two documents
	 * @param doc1
	 * @param doc2
	 * @return
	 */
	public static Double Distance(HashMap<Integer, Integer> doc1, HashMap<Integer, Integer> doc2) {
		Double dist = 0.0;
		for (Entry<Integer, Integer> entry : doc1.entrySet()) {
			//System.out.println("doc 1 get key"+entry.getKey());
			Integer doc1_key = entry.getKey();
			//System.out.println("doc 2"+doc2.toString());
			doc2_keys = doc2.keySet().iterator();
			
			try{
			if(doc2.containsKey(doc1_key)){
				//System.out.println("doc2.get(doc1_key)"+doc2.get(doc1_key));
				dist +=Math.pow(entry.getValue() - doc2.get(doc1_key), 2);
			}else{
				dist+=Math.pow(entry.getValue(), 2);
				
			}
			}catch(Exception e){
				System.out.println("distance key value error"+doc1_key);
				while(doc2_keys.hasNext()){
					System.out.println("doc 2 keys"+doc2_keys.next());
				}
				System.out.println("----------------");
				
				//System.out.println("if doc2 has key?"+doc2.containsKey(doc1_key));
				
			}
			// original
			
//			dist += doc2.containsKey(entry.getKey()) ?
//					Math.pow(entry.getValue() - doc2.get(entry.getKey()), 2) :
//						Math.pow(entry.getValue(), 2);
//				
				
		}
		for (Entry<Integer, Integer> entry : doc2.entrySet()) {
			dist += doc1.containsKey(entry.getKey()) ?
					0.0 : Math.pow(entry.getValue(), 2);
		}
		return Math.sqrt(dist);
	}
	
	public static double Distance( double[] x1, HashMap<Integer, Integer> doc ) {
		double dist = 0.0;
		for (int i = 0; i < x1.length; i++) {
			if ( doc.containsKey(i+1) ) {
				dist += Math.pow(x1[i] - doc.get(i+1), 2);
			}
			else {
				dist += Math.pow(x1[i], 2);
			}
		}
		return Math.sqrt(dist);
	}
	
	// added
	public static int HemmingDistance( Boolean[] x1,Boolean[] x2 ) {
		int dist = 0;
		for (int i = 0; i < x1.length; i++) {
			if(x1[i]!=x2[i]){
				dist+=1;
			}
		
		}
		return dist;
	}
	
	// added
	/** transform a document to double vector using array[i] = count
	 *  where i represent the term in the D space
	 * @param document
	 * @return
	 */
	public static HashMap<Integer,Double[]> DocToVector(HashMap<Integer, HashMap<Integer, Integer>> documents,int D) throws Exception {
		HashMap<Integer,Double[]> new_docs = new HashMap<Integer,Double[]>();
		// loop through a set of docs
		for(Entry<Integer,HashMap<Integer,Integer>> doc: documents.entrySet()){
			// D is the number of terms in the vocabulary
			Double[] doc_array = new Double[D];
			//populate doc_array with 0.0
			Arrays.fill(doc_array,new Double(0.0));
			// loop through all words of a doc
			for(Entry<Integer,Integer> entry: doc.getValue().entrySet()){
				
				doc_array[entry.getKey()-1]=Double.valueOf(entry.getValue());
				
			}
			new_docs.put(doc.getKey(), doc_array);
			
		}
		
	return new_docs;
	}
	


}
