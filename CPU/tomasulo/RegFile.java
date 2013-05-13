package tomasulo;

public class RegFile {
	
	public RegFile(int size)
	{
		this.data = new Data[size];
		
		for (int i=0; i<this.data.length; ++i)
		{
			this.data[i] = Data.normal(0);
		}
	}

	public Data[] data;
}
