package tomasulo;

public class Memory extends Resource{

	public double[] data;
	
	public Memory(int processCycles, int size) {
		super(processCycles);
		this.data = new double[size];
	}
	
	@Override
	protected double computeResult(Query query) {
		if (query instanceof LoadQuery)
		{
			return this.myComputeResult((LoadQuery)query);
		}
		else if (query instanceof StoreQuery)
		{
			return this.myComputeResult((StoreQuery)query);
		}
		else
		{
			Logger.Fatal("Illegal query to memory");
			return 0;
		}
	}
	
	private double myComputeResult(LoadQuery query)
	{
		return this.data[query.index];
	}
	
	private double myComputeResult(StoreQuery query)
	{
		this.data[query.index] = query.value;
		
		return 0;
	}
	
}
