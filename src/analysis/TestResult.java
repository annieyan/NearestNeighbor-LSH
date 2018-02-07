package analysis;

public class TestResult {
	public String method;
	public int n;
	public int D;
	public double alpha;
	public double avgTime;
	public double avgDistance;
	
	public TestResult(String method, int n, int D, double alpha, double avgTime, double avgDistance) {
		this.method = method;
		this.n = n;
		this.D = D;
		this.alpha = alpha;
		this.avgTime = avgTime;
		this.avgDistance = avgDistance;
	}

}
