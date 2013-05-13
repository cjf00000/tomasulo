package tomasulo;

public class Multiplier extends Resource {

	public Multiplier(int processCycles) {
		super(processCycles);
	}

	@Override
	protected double computeResult(Query query) {
		ComputeQuery cquery = (ComputeQuery)query;
		
		return cquery.a * cquery.b;
	}

}
