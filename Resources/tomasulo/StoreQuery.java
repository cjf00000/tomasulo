package tomasulo;

public class StoreQuery extends Query {
	
	public StoreQuery(int index, double value)
	{
		super();
		this.index = index;
		this.value = value;
	}
	
	public int index;
	public double value;
}
