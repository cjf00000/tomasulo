package tomasulo;

public class Adder extends Resource {

	public Adder(int processCycles) {
		super(processCycles);
	}

	@Override
	protected double computeResult(Query query) {
		ComputeQuery cquery = (ComputeQuery)query;
		return cquery.a + cquery.b;
	}

}
