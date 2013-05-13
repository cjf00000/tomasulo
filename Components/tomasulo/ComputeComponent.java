package tomasulo;

public abstract class ComputeComponent extends Component {

	public ComputeComponent(int numReservations, RegFile regFile,
			Resource resource) {
		super(numReservations, regFile, resource);
	}

	@Override
	protected void accept(Reservation reservation) {
		reservation.occupied = true;
		reservation.address = reservation.instruction.addr;
		reservation.srcData1 = this.regFile.data[reservation.instruction.src1];
		reservation.srcData2 = this.regFile.data[reservation.instruction.src2];
		
		this.regFile.data[reservation.instruction.dest].waitFor(reservation.destData);
	}

}
