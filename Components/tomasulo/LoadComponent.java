package tomasulo;

public class LoadComponent extends Component {
	
	public LoadComponent(int numReservations, RegFile regFile, Resource resource)	
	{
		super(numReservations, regFile, resource);
	}

	@Override
	protected Query createQuery(Reservation reservation) {
		return new LoadQuery(reservation.address);
	}

	@Override
	protected void accept(Reservation reservation) {
		reservation.occupied = true;
		reservation.address = reservation.instruction.addr;
		reservation.srcData1 = Data.normal(0);
		reservation.srcData2 = Data.normal(0);
		
		this.regFile.data[reservation.instruction.dest].waitFor(reservation.destData);
	}

	@Override
	protected boolean ifAccept(Instruction instruction) {
		if (instruction.type == Instruction.Type.LD)
			return true;
		
		return false;
	}
}
