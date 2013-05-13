package tomasulo;

public class ComputeQuery extends Query {
	
	public ComputeQuery(double a, double b)
	{
		super();
		this.a = a;
		this.b = b;
	}
	
	public double a, b;
}
