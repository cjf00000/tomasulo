package tomasulo;

public class Reservation {
	public Reservation()
	{
		this.occupied = false;
		
		this.destData = Data.dest(0);
		this.srcData1 = Data.normal(0);
		this.srcData2 = Data.normal(0);
	}

	public boolean isOccupied()
	{
		return this.occupied;
	}
	
	public boolean isReady() {
		return this.occupied && this.srcData1.isReady() && this.srcData2.isReady();
	}
	
	public void acceptMessage(int q, double v) {
		this.destData.acceptMessage(q, v);
		this.srcData1.acceptMessage(q, v);
		this.srcData2.acceptMessage(q, v);
	}	
	
	@Override
	public String toString()
	{
		return "Reservation { instruction=" + instruction + " destData=" + destData + " srcData1=" + srcData1 + " srcData2=" + srcData2 + " occupied=" + occupied
				+ " address=" + address + " }";
	}
	
	public Instruction instruction;
	public Data destData, srcData1, srcData2;
	public Boolean occupied;
	public int address;
}
