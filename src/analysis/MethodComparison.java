package analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import data.DocumentData;
import data.RandomData;
import kdtree.KDTree;
import methods.GaussianRandomProjection;
import methods.LocalitySensitiveHash;
import methods.NeighborDistance;
import util.EvalUtil;

public class MethodComparison {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		HashMap<Integer, HashMap<Integer, Integer>> docdata = DocumentData.ReadInData("sim_docdata.mtx", true);
		HashMap<Integer, HashMap<Integer, Integer>> testdata = DocumentData.ReadInData("test_docdata.mtx", true);
		System.err.println("Number of Documents: " + docdata.keySet().size());
		System.err.println("Number of Test Documents: " + testdata.keySet().size());
		Integer D = 1000;
		Integer n=100000;
		Integer nTest=500;
		//TODO - run tests here
		
		//System.out.println(testdata.toString());
		// test LSH
//		int[] m_vals= {5,10,20};
//		Collection<TestResult>  run_time= TestLSH(docdata,testdata,D,m_vals,n,nTest);
//		Iterator<TestResult> iter = run_time.iterator();
////		while(iter.hasNext()){
////			System.out.println("method:"+iter.next().method+"\n");	
////			System.out.println("m value:"+iter.next().alpha+"\n");	
////			System.out.println("run time:"+iter.next().avgTime+"\n");
////			
////			//System.out.println("mean dist:"+iter.next().avgDistance);
////		
////		}

		//*******test KDTree ***********
		System.out.println("test with KD tree!");
		double[] alphas = {1,5,10};
		Collection<TestResult>  run_time_kd= TestKDTree(docdata,testdata,n,D,nTest,alphas);
		Iterator<TestResult> iter_kd = run_time_kd.iterator();
//		while(iter_kd.hasNext()){
//			System.out.println("run time:"+iter_kd.next().avgTime+"alpha value:"+);
//			System.out.println("alpha value:"+iter_kd.next().alpha+"\n");	
//			System.out.println("mean dist:"+iter_kd.next().avgDistance);
//		}
		while(iter_kd.hasNext()){
			TestResult result = iter_kd.next();
			System.out.println("run time:"+result.avgTime+"-- dist:"+result.avgDistance+"-- alpha:"+result.alpha+"\n");
		}
		
		
		// *******  test random projection******
//		System.out.println("test with random projection!");
//		int[] m_vals= {5,10,20};
//		
//		int[] alphas = {1,5,10};
//		for(int alpha:alphas){
//			System.out.println("alpha="+alpha+"\n");
//			Collection<TestResult>  run_time_grp= TestRandomProj(docdata,testdata,D,m_vals,n,nTest,alpha);
//			Iterator<TestResult> iter_grp = run_time_grp.iterator();
//			while(iter_grp.hasNext()){
//				System.out.println("run time:"+iter_grp.next().avgTime);
//				System.out.println("m value:"+iter_grp.next().alpha);	
//				System.out.println("mean dist:"+iter_grp.next().avgDistance);
//			}
			
//		}
		
		
		
//		
		
	}
	
	
	
	public static Collection<TestResult> TestLSH(
			HashMap<Integer, HashMap<Integer, Integer>> docdata,
			HashMap<Integer, HashMap<Integer, Integer>> testdata,
			Integer D, int[] m_vals,int n,Integer nTest) throws Exception {
		
		
		// store time used
		ArrayList<TestResult> times = new ArrayList<TestResult>();
		int depth = 3;
		for (int a = 0; a < m_vals.length; a++) {
			// build Hashed Documents
		
			LocalitySensitiveHash LSH = new LocalitySensitiveHash(docdata,D,m_vals[a]);
			long startTime = System.nanoTime();
			double cumDist = 0.0;
			
			// loop test data
			for (Entry<Integer, HashMap<Integer, Integer>> entry : testdata.entrySet()) {
				HashMap<Integer, Integer> test_point = entry.getValue();
				
				//System.out.println("test_point"+test_point.toString());
				NeighborDistance nearest = LSH.NearestNeighbor(test_point,depth);
				Double nearest_dist = nearest.distance;
				//System.out.print("NeighborDistance id"+nearest.docId+"\n");
				//System.out.println("NeighborDistance dist"+nearest_dist+"\n");
				//System.out.print("NeighborDistance document"+docdata.get(nearest.docId)+"\n");
				if (nearest.docId!=0){
					//cumDist += EvalUtil.Distance(test_point, docdata.get(nearest.docId));
					cumDist+=nearest_dist;
				}else{
					cumDist+=0.0;
				}
				
				
			}
			System.out.println("LSH for m="+m_vals[a]);
			System.out.println("ave dist="+cumDist / (nTest + 0.0)+"\n");
			System.out.println("ave run time="+	((System.nanoTime() - startTime) / Math.pow(10, 9)) / (nTest + 0.0)+"\n");
			
			times.add(new TestResult("LSH", n, D, m_vals[a],
					((System.nanoTime() - startTime) / Math.pow(10, 9)) / (nTest + 0.0),
					cumDist / (nTest + 0.0)));
		}
		return times;
	}
	
	public static Collection<TestResult> TestKDTree(
			HashMap<Integer, HashMap<Integer, Integer>> docdata,
			HashMap<Integer, HashMap<Integer, Integer>> testdata,
			Integer n, Integer D, Integer nTest, double[] alphas) throws Exception {
//		HashMap<Integer, Double[]> documents = RandomData.RandomDataset(n, D);
//		HashMap<Integer, Double[]> testPoints = RandomData.RandomDataset(nTest, D);

		HashMap<Integer, Double[]> docdata_new = EvalUtil.DocToVector(docdata,D);
		HashMap<Integer, Double[]> test_new = EvalUtil.DocToVector(testdata,D);
		
		
		double[] key = new double[D];
		KDTree randTree = new KDTree(D);
		for (Entry<Integer, Double[]> document : docdata_new.entrySet()) {
			for (int i = 0; i < D; i++) {
				key[i] = document.getValue()[i];
			}
			randTree.insert(key, document.getKey());
		}
		
		//kdt = new KDTree(m);
//		for (Integer docId : docdata_new.keySet()) {
//			randTree.insert(docdata_new.get(docId), docId);
//				
//			}
//			
//			
//			randTree.insert(docdata_new.get(docId), docId);
//		}	
//		
		
		ArrayList<TestResult> times = new ArrayList<TestResult>();
		for (int a = 0; a < alphas.length; a++) {
			long startTime = System.nanoTime();
			double cumDist = 0.0;
			for (Double[] testPt : test_new.values()) {
				long start_time_inner = System.nanoTime();
				System.out.println("testing KD tree data point");
				for (int i = 0; i < D; i++) {
					key[i] = testPt[i];
				}
				Integer docId = (Integer)randTree.nearest(key, alphas[a]);
				cumDist += EvalUtil.Distance(testPt, docdata_new.get(docId));
			
			}
			//System.out.println("time for one point"+((System.nanoTime() - start_time_inner) / Math.pow(10, 9)) / (nTest + 0.0));
			System.out.println("KDTree for alphas="+alphas[a]);
			System.out.println("ave dist="+cumDist / (nTest + 0.0));
			System.out.println("ave run time="+	((System.nanoTime() - startTime) / Math.pow(10, 9)) / (nTest + 0.0)+"\n");
			
		
			
			times.add(new TestResult("KDTree", n, D, alphas[a],
					((System.nanoTime() - startTime) / Math.pow(10, 9)) / (nTest + 0.0),
					cumDist / (nTest + 0.0)));
		}
		return times;
	}
	
	
	
	public static Collection<TestResult> TestRandomProj(
			HashMap<Integer, HashMap<Integer, Integer>> docdata,
			HashMap<Integer, HashMap<Integer, Integer>> testdata,
			Integer D, int[] m_vals,int n,Integer nTest, int alpha) throws Exception {
		// store time used
		ArrayList<TestResult> times = new ArrayList<TestResult>();
		for (int a = 0; a < m_vals.length; a++) {
			// build Hashed Documents
		
			GaussianRandomProjection GRP = new GaussianRandomProjection(docdata,D,m_vals[a]);
			long startTime = System.nanoTime();
			double cumDist = 0.0;
			
			// loop test data
			for (Entry<Integer, HashMap<Integer, Integer>> entry : testdata.entrySet()) {
				HashMap<Integer, Integer> test_point = entry.getValue();
				
				//System.out.println("test_point"+test_point.toString());
				NeighborDistance nearest = GRP.NearestNeighbor(test_point,alpha);
				Double nearest_dist = nearest.distance;
				//System.out.print("NeighborDistance id"+nearest.docId+"\n");
				//System.out.println("NeighborDistance dist"+nearest_dist+"\n");
				//System.out.print("NeighborDistance document"+docdata.get(nearest.docId)+"\n");
				if (nearest.docId!=0){
					//cumDist += EvalUtil.Distance(test_point, docdata.get(nearest.docId));
					cumDist+=nearest_dist;
				}else{
					cumDist+=0.0;
				}
				
				
			}
			System.out.println("GRP for m="+m_vals[a]);
			System.out.println("ave dist="+cumDist / (nTest + 0.0));
			System.out.println("ave run time="+	((System.nanoTime() - startTime) / Math.pow(10, 9)) / (nTest + 0.0)+"\n");
			
			times.add(new TestResult("GRP", n, D, m_vals[a],
					((System.nanoTime() - startTime) / Math.pow(10, 9)) / (nTest + 0.0),
					cumDist / (nTest + 0.0)));
		}
		return times;
	}
	
	

}
