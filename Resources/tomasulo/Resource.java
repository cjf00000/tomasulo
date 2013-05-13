package tomasulo;

public abstract class Resource {
	
	public Resource(int processCycles)
	{
		this.occupied = false;
		this.processCycles = processCycles;
		this.owner = null;
	}
	
	public boolean isOccupied()
	{
		return this.occupied;
	}
	
	public boolean tryQuery(Query query)
	{		
		if (this.occupied)			
			return false;
		
		Logger.Info("Begin to process query " + query);
		
		this.owner = query.source;
		this.result = this.computeResult(query);
		this.occupied = true;
		this.countDown = this.processCycles + 1;		
		
		return true;
	}
	
	public void execute()
	{
		if (!occupied)
		{
			return;
		}
		
		--this.countDown;
		Logger.Debug("Resource is executing, countdown =" + this.countDown);
		
		if (this.countDown==0)
		{
			this.occupied = false;
		}
	}	
	
	public double getResult()
	{
		return this.result;
	}
	
	protected abstract double computeResult(Query query);
	
	protected boolean occupied;
	protected int processCycles;
	protected int countDown;
	protected double result;
	public Component owner;
}
