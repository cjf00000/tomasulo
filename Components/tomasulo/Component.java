package tomasulo;

import java.util.ArrayList;

public abstract class Component {
	
	public Component(int numReservations, RegFile regFile, Resource resource)
	{
		this.reservations = new ArrayList<Reservation>();
		
		for (int i=0; i<numReservations; ++i)
		{
			this.reservations.add(new Reservation());
		}
		
		this.regFile = regFile;
		this.resource = resource;
	}
		
	public Boolean isFull()
	{
		for (Reservation reservation : this.reservations)
		{
			if (!reservation.isOccupied())
			{
				return false;
			}
		}
		
		return true;
	}
	
	public void accept(Instruction instruction)
	{
		Logger.Info("Component: get instruction " + instruction);
		
		for (Reservation reservation : this.reservations)
		{
			if (!reservation.isOccupied())
			{
				reservation.instruction = instruction;
								
				this.accept(reservation);
				this.tryExecute(reservation);
				
				return;
			}
		}
		
		Logger.Fatal("Component: no available reservations");
	}
	
	public Data countinueExecute()
	{	
		if (!resource.occupied || resource.owner!=this)
		{
			return null;
		}
			
		resource.execute();
		
		if (!resource.occupied)
		{
			this.runningReservation.destData.value = resource.getResult();
			this.runningReservation.occupied = false;
			
			Logger.Info("Instruction " + this.runningReservation.instruction + " finished, return value = " + this.runningReservation.destData.value);
			
			return this.runningReservation.destData;
		}
		
		return null;
	}		
	
	public void acceptMessage(int q, double v)
	{
		for (Reservation reservation : this.reservations)
		{
			reservation.acceptMessage(q, v);
			
			this.tryExecute(reservation);
		}
	}
	
	private void tryExecute(Reservation reservation) {
		if (reservation.isReady())
		{
			Query query = this.createQuery(reservation);
			query.source = this;
			this.resource.tryQuery(query);
			this.runningReservation = reservation;
		}		
	}
	
	protected abstract Query createQuery(Reservation reservation);	
	protected abstract void accept(Reservation reservation);
	protected abstract boolean ifAccept(Instruction instruction);
	
	private ArrayList<Reservation> reservations;
	private Reservation runningReservation;
	protected RegFile regFile;
	protected Resource resource;
}
