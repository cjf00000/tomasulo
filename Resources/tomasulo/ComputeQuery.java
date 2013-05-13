package tomasulo;

public class ComputeQuery extends Query {
	
	public ComputeQuery(double a, double b)
	{
		super();
		this.a = a;
		this.b = b;
	}
	
	public ComputeQuery(double a, double b, boolean isDiv)
	{
		super();
		this.a = a;
		this.b = b;
		this.isDiv = isDiv;
	}
	
	public double a, b;
	public boolean isDiv;
}
