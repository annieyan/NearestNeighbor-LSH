package methods;

import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import util.EvalUtil;
import util.HashUtil;
import kdtree.KDTree;

public class GaussianRandomProjection {
	// Projection vectors
	public double[][] projVectors;
	// Hash of projected bin to document ids
	public KDTree kdt;
	// reference to the original documents
	private HashMap<Integer, HashMap<Integer, Integer>> docs;
	// Dimensionality of the data
	private Integer D;
	// Number of projections to use (dimensionality of LSH)
	private Integer m;

	public GaussianRandomProjection(
			HashMap<Integer, HashMap<Integer, Integer>> documents,
			Integer D,
			Integer m) throws Exception {
		
		this.docs = documents;
		this.D = D;
		this.m = m;
		projVectors = Helper.CreateProjectionVectors(D, m);
		kdt = new KDTree(m);
		for (Integer docId : documents.keySet()) {
			kdt.insert(HashDocument(documents.get(docId)), docId);
		}		
	}

	/**Gets the (approximate) nearest neighbor to the given document
	 * @param document
	 * @param alpha for the KD Tree
	 * @return
	 */
	public NeighborDistance NearestNeighbor(HashMap<Integer, Integer> document, double alpha) throws Exception {
		double[] hashedDoc = HashDocument(document);
		Integer nearestId = (Integer)kdt.nearest(hashedDoc, alpha);
		NeighborDistance nearest = new NeighborDistance(nearestId, EvalUtil.Distance(document, docs.get(nearestId)));
		return nearest;
	}
	
	/**Hashes a document to a double array using the set of projection vectors
	 * @param document
	 * @return
	 */
	public double[] HashDocument(HashMap<Integer, Integer> document) throws Exception {
		double[] hashedDoc = new double[m];
		//TODO - Fill in code for creating the hashed document
		//throw new Exception("Please implement the GaussianRandomProjection.HashDocument method");
		//return hashedDoc;
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
		projected_max = projected_max.mapDivideToSelf(Math.sqrt(m));
//		System.out.println("hased_documents"+projected_max.toString());
		hashedDoc = projected_max.toArray();
		return hashedDoc;
	}
	
	/**Projects a document onto a projection vector, returning the double value
	 * @param document
	 * @param projVector
	 * @return
	 */
	private static double ProjectDocument(HashMap<Integer, Integer> document, double[] projVector) throws Exception {
		double dotProd = 0.0;
		//TODO - Fill in code for projecting the document
		throw new Exception("Please implement the GaussianRandomProjection.ProjectDocument method");
		//return dotProd;
	}

}
