package tomasulo;

// TODO Çø·ÖdestdataºÍsrcdata
public class Data {	
	public int reference;
	
	/** Iff reference = 0, this field is valid **/
	public double value;
	
	static private int cnt = 0;
	
	static public Data normal(double value)
	{
		Data ret = new Data();
		ret.reference = 0;
		ret.value = value;
		
		return ret;
	}
	
	static public Data dest(double value)
	{
		Data ret = new Data();
		ret.reference = ++cnt;
		ret.value = value;
		
		return ret;
	}
	
	private Data() {}

	public boolean isReady() {
		return reference==0;
	}

	public void acceptMessage(int q, double v) {
		if (this.reference == q)
		{
			this.value = v;
			this.reference = 0;			
		}
	}
	
	@Override
	public String toString()
	{
		return "{ q=" + this.reference + " v=" + this.value + " }";
	}
}
