package tomasulo;

public class MultiplyDivideComponent extends ComputeComponent {

	public MultiplyDivideComponent(int numReservations, RegFile regFile,
			Resource resource) {
		super(numReservations, regFile, resource);
	}

	@Override
	protected boolean ifAccept(Instruction instruction) {
		return (instruction.type == Instruction.Type.MULD || instruction.type == Instruction.Type.DIVD);
	}

	@Override
	protected Query createQuery(Reservation reservation) {
		return new ComputeQuery(reservation.srcData1.value, reservation.srcData2.value, reservation.instruction.type == Instruction.Type.DIVD);
	}

}
