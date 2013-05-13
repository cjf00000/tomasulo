package tomasulo;

public class MultiplyDivider extends Resource {

	public MultiplyDivider(int processCyclesMul, int processCyclesDiv) {
		super(999999);
		this.processCyclesMul = processCyclesMul;
		this.processCyclesDiv = processCyclesDiv;
	}

	@Override
	protected double computeResult(Query query) {
		ComputeQuery cquery = (ComputeQuery)query;
	
		if (cquery.isDiv)
			return cquery.a / cquery.b;
		else
			return cquery.a * cquery.b;
	}
	
	@Override
	public boolean tryQuery(Query query)
	{
		boolean ret = super.tryQuery(query);
		
		if (!ret)
			return false;
		
		ComputeQuery cquery = (ComputeQuery)query;
		
		if (cquery.isDiv)
			this.countDown = this.processCyclesDiv + 1;
		else 
			this.countDown = this.processCyclesMul + 1;
		
		return ret;
	}

	private int processCyclesMul, processCyclesDiv;
}
