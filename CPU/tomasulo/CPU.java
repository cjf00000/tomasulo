package tomasulo;

import java.util.ArrayList;
import java.util.HashMap;

public class CPU {
	
	public CPU()
	{
		// Resources
		Adder adder = new Adder(2);
		Multiplier multiplier = new Multiplier(10);
		Divider divider = new Divider(40);
		this.memory = new Memory(2, 4096);
		
		// Reg
		this.regFile = new RegFile(16);
		
		// Component
		this.components = new ArrayList<Component>();
		this.components.add(new LoadComponent(3, this.regFile, memory));
		
		// OpQueue
		this.opQueue = new OpQuene(components);
	}
	
	/**
	 * Get a list of pending instructions.
	 * 
	 * @return A list of Instruction, from old to new.
	 * @see	Instruction
	 */
	public ArrayList<Instruction> getPendingInstructionQueue()
	{
		throw new NotImplementedException();
	}
	
	/**
	 * Get a list of running instructions.
	 * 
	 * @return A list of Instruction, from old to new.
	 * @see	Instruction
	 */
	public ArrayList<Instruction> getRunningInstructionQueue()
	{
		throw new NotImplementedException();
	}
	
	/**
	 * Add an instruction to pending instruction queue.
	 * 
	 * @param The instruction to be added.
	 */
	public void addInstruction(Instruction instruction)
	{
		this.opQueue.EnQueue(instruction);
	}

	/**
	 * Make the CPU time elapse by 1 cycle.
	 */
	public void onTick()
	{
		Logger.Info("Tick");
		
		// Issue an instruction
		opQueue.onTick();
		
		// Let components run
		ArrayList<Data> changes = new ArrayList<Data>();
		for (Component component : this.components)
		{
			Data data = component.countinueExecute();
			
			if (data!=null)
			{
				changes.add(data);
			}
		}
		
		// Broadcast changes
		for (Data data : changes)
		{
			for (Component component : this.components)
			{
				component.acceptMessage(data.reference, data.value);
			}
		}
	}
	
	public double getMemory(int index)
	{
		return this.memory.data[index];
	}
	
	public void setMemory(int index, double value)
	{
		this.memory.data[index] = value;
	}
	
	public static void main(String args[])
	{
		CPU cpu = new CPU();
		
		cpu.addInstruction(Instruction.mem(Instruction.Type.LD, 0, 0));		
		cpu.onTick();
		cpu.onTick();
		cpu.onTick();
		cpu.onTick();
	}
	
	private OpQuene opQueue;
	private ArrayList<Component> components;
	private RegFile regFile;
	private Memory memory;
}
