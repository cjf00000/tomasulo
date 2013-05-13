package tomasulo;

public class AddSubtractComponent extends ComputeComponent {

	public AddSubtractComponent(int numReservations, RegFile regFile,
			Resource resource) {
		super(numReservations, regFile, resource);
	}

	@Override
	protected boolean ifAccept(Instruction instruction) {
		return (instruction.type == Instruction.Type.ADDD || instruction.type == Instruction.Type.SUBD);
	}

	@Override
	protected Query createQuery(Reservation reservation) {
		if (reservation.instruction.type == Instruction.Type.ADDD)
		{
			return new ComputeQuery(reservation.srcData1.value, reservation.srcData2.value);
		}
		else if (reservation.instruction.type == Instruction.Type.SUBD)
		{
			return new ComputeQuery(reservation.srcData1.value, -reservation.srcData2.value);
		}
		else
		{
			Logger.Fatal("AddSbutractComponent: Unsupported operation " + reservation.instruction.type);
			return new ComputeQuery(0, 0);
		}
	}

}
