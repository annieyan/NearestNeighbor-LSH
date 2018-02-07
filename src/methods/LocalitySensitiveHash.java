package methods;
// added external math libraries
import org.apache.commons.math3.*;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.math.*;


import util.EvalUtil;
import util.HashUtil;


public class LocalitySensitiveHash {

	// Projection vectors
	public double[][] projVectors;
	// Hash of projected bin to document ids
	public HashMap<Integer, HashSet<Integer>> hashedDocuments;
	// reference to the original documents
	private HashMap<Integer, HashMap<Integer, Integer>> docs;
	// Dimensionality of the data
	private Integer D;
	// Number of projections to use (dimensionality of LSH)
	private Integer m;
	
	public HashMap<Integer, Boolean[]> hasedDocments_bin_dist;
	

	public LocalitySensitiveHash(
			HashMap<Integer, HashMap<Integer, Integer>> documents,
			Integer D,
			Integer m) throws Exception {
		
		this.docs = documents;
		this.D = D;
		this.m = m;
		projVectors = Helper.CreateProjectionVectors(D, m);
		//System.out.println("initailizating LHS"+this.D+this.m+projVectors.length);
		BuildHashedDocuments();
		
	}
	
	/**Gets the (approximate) nearest neighbor to the given document
	 * @param document - the document to find the nearest neighbor for
	 * @param depth - the maximum number of bits to change concurrently
	 * @return
	 */
	public NeighborDistance NearestNeighbor(HashMap<Integer, Integer> document, Integer depth) throws Exception {
		Boolean[] hashedDoc = HashDocument(document);
		NeighborDistance nearest = NearestNeighbor(document, hashedDoc, null, depth, 0);
		return nearest;
	}
		
	/**
	 * @param document - the document to find the nearest neighbor for
	 * @param hashedDoc
	 * @param curNearest
	 * @param depth
	 * @param nextIndex
	 */
	private NeighborDistance NearestNeighbor(
			HashMap<Integer, Integer> document,
			Boolean[] hashedDoc,
			NeighborDistance curNearest,
			Integer depth,
			Integer nextIndex) throws Exception {
		
		if ( depth < 0 ) {
			return curNearest;
		}
		if ( null == curNearest ) {
			curNearest = new NeighborDistance(0, Double.MAX_VALUE);
		}
		curNearest = CheckBin(document, hashedDoc, curNearest);
		if ( depth > 0 ) {
			// check the bins one away from the current bin
			// if we still have more depth to go
			// // check the bins within 3 hemming distance of the given bin
			for (int j = nextIndex; j < m; j++) {
				hashedDoc[j] = !hashedDoc[j];
				NearestNeighbor(document, hashedDoc, curNearest, depth-1, j+1);
				hashedDoc[j] = !hashedDoc[j];				
			}
		}
		
		return curNearest;
	}
	
	/**
	 * Checks the documents that are hashed to the given bin
	 * and updates with the nearest neighbor found
	 * @param document - the document to find the nearest neighbor for
	 * @param bin - the bin to search
	 * @param curNearest - the current nearest neighbor
	 */
	private NeighborDistance CheckBin(
			HashMap<Integer, Integer> document, 
			Boolean[] bin,
			NeighborDistance curNearest) throws Exception {
	
		//       Code should look through all the documents in a bin and
		//       update curNearest with the nearest one found, if closer than curNearest already is
		//throw new Exception("Please implement the LocalitySensitiveHash.CheckBin method");
		//System.out.println("number of bins"+hashedDocuments.size());	
		
		Integer bin_id = ConvertBooleanArrayToInteger(bin);
//		/////////////////
//		//System.out.println("bin id"+bin_id);
		if(hashedDocuments.containsKey(bin_id)){
			HashSet<Integer> doc_in_bin = hashedDocuments.get(bin_id);
			try{
				for (Integer docid: doc_in_bin ){
					// get distance and update curnearest
				
					
						HashMap<Integer,Integer> doc_in_bin_hash = docs.get(docid);
						
						Double dist=EvalUtil.Distance(doc_in_bin_hash,document);
						if(dist<curNearest.distance){
							curNearest = new NeighborDistance(docid,dist);
							//return curNearest;
							//System.out.println("nearest points updated!"+docid+"dist:"+dist);
				
					
				}}
				
			}catch(Exception e){
				System.out.println("doc id or doc bin does not exist");
				System.out.println("binid"+bin_id);
				
			}
			
		}
	
		
		
		// check the bins within 3 hemming distance of the given bin
//		for (Entry<Integer, Boolean[]> entry : hasedDocments_bin_dist.entrySet()) {
//			int hem_dist = EvalUtil.HemmingDistance(entry.getValue(),bin);
//			if(hem_dist<3||hem_dist==3){
//
//				Integer bin_id = ConvertBooleanArrayToInteger(entry.getValue());
//				/////////////////
//				//System.out.println("bin id"+bin_id);
//				HashSet<Integer> doc_in_bin = hashedDocuments.get(bin_id);
//				
//				try{
//					for (Integer docid: doc_in_bin ){
//						// get distance and update curnearest
//					
//						
//							HashMap<Integer,Integer> doc_in_bin_hash = docs.get(docid);
//							
//							Double dist=EvalUtil.Distance(doc_in_bin_hash,document);
//							if(dist<curNearest.distance){
//								curNearest = new NeighborDistance(docid,dist);
//								//return curNearest;
//								//System.out.println("nearest points updated!"+docid+"dist:"+dist);
//					
//						
//					}}
//					
//				}catch(Exception e){
//					System.out.println("doc id or doc bin does not exist");
//					System.out.println("binid"+bin_id);
//					
//				}
//						
//			}			
//		}
			
		// iterate through doc ids in a bin
	//	System.out.println("number of docs in this bin"+doc_in_bin.size());
				
	
		return curNearest;
	}
	
	/**
	 * Builds the hashtable of documents
	 * @param documents
	 */
	private void BuildHashedDocuments() throws Exception {
		hashedDocuments = new HashMap<Integer, HashSet<Integer>>();
		hasedDocments_bin_dist = new HashMap<Integer, Boolean[]>();
		Integer bin;
		for (Entry<Integer, HashMap<Integer, Integer>> entry : docs.entrySet()) {
			bin = GetBin(entry.getValue());
			if ( !hashedDocuments.containsKey(bin) ) {
				hashedDocuments.put(bin, new HashSet<Integer>());
				//hasedDocments_bin_dist.put(bin, new Boolean[m]);
				hasedDocments_bin_dist.put(bin, HashDocument(entry.getValue()));
			}
			hashedDocuments.get(bin).add(entry.getKey());	
			
		}		
	}
	
	/**
	 * Gets the bin where a certain document should be stored
	 * @param document
	 * @return
	 */
	private Integer GetBin(HashMap<Integer, Integer> document) throws Exception {
		return ConvertBooleanArrayToInteger(HashDocument(document));
	}
	
	/**Hashes a document to a boolean array using the set of projection vectors
	 * Hashed to a boolean arrray which specifies the index of a bin
	 * @param document
	 * @return
	 */
	public Boolean[] HashDocument(HashMap<Integer, Integer> document) throws Exception {
		Boolean[] hashedDoc = new Boolean[m];
	
		// create array for this document
			
		double[] doc_array = new double[D];
		for(Entry<Integer,Integer> entry: document.entrySet()){
			doc_array[entry.getKey()-1]=entry.getValue();
		}
	//	System.out.println("projector vector len:"+projVectors.length);
		//RealMatrix projVector_trans = new Array2DRowRealMatrix(projVectors);
		RealMatrix projVector_trans = MatrixUtils.createRealMatrix(projVectors);
		
		ArrayRealVector doc_array_trans = new ArrayRealVector(doc_array);
		// get PHI presentation
		RealVector projected_max = projVector_trans.operate(doc_array_trans);
		
		//System.out.println(projVector_trans.getRowDimension());    // 2
		//System.out.println(projVector_trans.getColumnDimension()); // 2
		projected_max = projected_max.mapDivideToSelf(Math.sqrt(m));
//		System.out.println("hased_documents"+projected_max.toString());
		double[] project_Array = projected_max.toArray();
		// hash to 0/1 projection
		//System.out.println("hased_documents"+project_Array.toString());
		for (int i =0; i< project_Array.length;i++){
			hashedDoc[i]=HashUtil.hashToBool(project_Array[i]);
//			System.out.println(hashedDoc[i]);
		}
//		System.out.println("hashed boolean doc"+hashedDoc.toString());
		
		
		
				
//		projVectors
//		throw new Exception("Please implement the LocalitySensitiveHash.HashDocument method");
		return hashedDoc;
	}
	
	/**Projects a document onto a projection vector for a boolean result
	 * @param document
	 * @param projVector
	 * @return false if the projection is negative, true if the projection is positive
	 */
	private Boolean ProjectDocument(HashMap<Integer, Integer> document, double[] projVector) throws Exception {
		double dotProd = 0.0;
		//TODO - Fill in code for projecting the document
		throw new Exception("Please implement the LocalitySensitiveHash.ProjectDocument method");
		//return dotProd;
	}
	
	/**Converts a boolean array to the corresponding integer value
	 * this corresponds to Bin identifier?
	 * @param boolArray
	 * @return
	 */
	private Integer ConvertBooleanArrayToInteger(Boolean[] boolArray) {
		Integer value = 0;
		for (int i = 0; i < boolArray.length; i++) {
			value += boolArray[i] ? (int)Math.round(Math.pow(2, i)) : 0;
		}
		return value;
	}

}
