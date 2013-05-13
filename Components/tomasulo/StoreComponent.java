package tomasulo;

public class StoreComponent extends Component {

	public StoreComponent(int numReservations, RegFile regFile,
			Resource resource) {
		super(numReservations, regFile, resource);
	}

	@Override
	protected Query createQuery(Reservation reservation) {
		return new StoreQuery(reservation.address, reservation.srcData1.value);
	}

	@Override
	protected void accept(Reservation reservation) {		
		reservation.occupied = true;
		reservation.address = reservation.instruction.addr;
		reservation.srcData1 = this.regFile.data[reservation.instruction.reg];
		reservation.srcData2 = Data.normal(0);	
	}

	@Override
	protected boolean ifAccept(Instruction instruction) {
		if (instruction.type == Instruction.Type.ST)
			return true;
		
		return false;
	}

}
