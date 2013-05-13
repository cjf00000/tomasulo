package tomasulo;

import java.util.ArrayList;
import java.util.HashMap;

public class CPU {
	
	public CPU()
	{
		// Resources
		Adder adder = new Adder(2);
		MultiplyDivider divider1 = new MultiplyDivider(10, 40);
		MultiplyDivider divider2 = new MultiplyDivider(10, 40);
		this.memory = new Memory(2, 4096);
		
		// Reg
		this.regFile = new RegFile(16);
		
		// Component
		this.components = new ArrayList<Component>();
		this.components.add(new LoadComponent(3, this.regFile, memory));
		this.components.add(new StoreComponent(3, this.regFile, memory));
		this.components.add(new AddSubtractComponent(3, regFile, adder));
		this.components.add(new MultiplyDivideComponent(2, regFile, divider1));
		this.components.add(new MultiplyDivideComponent(2, regFile, divider2));
		
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
		return this.opQueue.pendingQ;
	}
	
	/**
	 * Get a list of running instructions.
	 * 
	 * @return A list of Instruction, from old to new.
	 * @see	Instruction
	 */
	public ArrayList<Instruction> getRunningInstructionQueue()
	{
		return this.opQueue.runningQ;
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
			for (Data data2 : this.regFile.data)
			{
				data2.acceptMessage(data.reference, data.value);
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
	
	public Data getReg(int index)
	{
		return this.regFile.data[index];
	}
	
	public void setReg(int index, Data data)
	{
		this.regFile.data[index] = data;
	}
	
	public void setReg(int index, double value)
	{
		this.regFile.data[index].value = value;
	}
	
	public static void main(String args[])
	{
		CPU cpu = new CPU();
		
		// Test Load
		cpu.setMemory(0, 1992.23);
		
		cpu.addInstruction(Instruction.mem(Instruction.Type.LD, 0, 0));		
		cpu.onTick();
		cpu.onTick();
		cpu.onTick();
		cpu.onTick();
		
		Logger.Debug("Load result (should be 1992.23) = " + cpu.getReg(0));
		
		// Test Store
		cpu.addInstruction(Instruction.mem(Instruction.Type.ST, 0, 1));		
		cpu.onTick();
		cpu.onTick();
		cpu.onTick();
		cpu.onTick();
		
		Logger.Debug("Store result (should be 1992.23) = " + cpu.getMemory(1));
		
		// Test Add
		cpu.setReg(1, 2222.22);
		cpu.addInstruction(Instruction.reg(Instruction.Type.ADDD, 2, 0, 1));
		cpu.onTick();
		cpu.onTick();
		cpu.onTick();
		cpu.onTick();
		Logger.Debug("Add result (should be 4214.45) = " + cpu.getReg(2));
		
		for (int i=0; i<16; ++i)
			Logger.Debug(cpu.getReg(i).toString());
		
		// Test subtract
		cpu.addInstruction(Instruction.reg(Instruction.Type.SUBD, 3, 0, 1));
		cpu.onTick();		
		cpu.onTick();
		cpu.onTick();
		cpu.onTick();
		Logger.Debug("Sub result (should be -229.99) = " + cpu.getReg(3));
		for (int i=0; i<16; ++i)
			Logger.Debug(cpu.getReg(i).toString());
		
		// Test multiply
		cpu.addInstruction(Instruction.reg(Instruction.Type.MULD, 4, 0, 1));
		for (int i=0; i<14; ++i) cpu.onTick();
		Logger.Debug("Mul result (should be 4427173.3506) = " + cpu.getReg(4));
		
		// Test divide
		cpu.addInstruction(Instruction.reg(Instruction.Type.DIVD, 5, 0, 1));
		for (int i=0; i<44; ++i) cpu.onTick();
		Logger.Debug("Div result (should be 0.8965044) = " + cpu.getReg(5));
		
		// Test RAW dependency
		cpu.addInstruction(Instruction.reg(Instruction.Type.ADDD, 6, 0, 1));
		cpu.addInstruction(Instruction.reg(Instruction.Type.ADDD, 7, 0, 6));
		for (int i=0; i<12; ++i) cpu.onTick();
		Logger.Debug("Add result (should be 6206.68) = " + cpu.getReg(7));
		
		// Test WAW dependency
		cpu.addInstruction(Instruction.reg(Instruction.Type.MULD, 8, 0, 1));
		cpu.addInstruction(Instruction.reg(Instruction.Type.ADDD, 8, 0, 1));
		for (int i=0; i<15; ++i) cpu.onTick();
		Logger.Debug("Mul result (should be 4214.45) = " + cpu.getReg(8));
		
		// Test WAR dependency
		cpu.addInstruction(Instruction.reg(Instruction.Type.MULD, 11, 0, 1));				
		cpu.addInstruction(Instruction.reg(Instruction.Type.ADDD, 12, 9, 11));
		cpu.addInstruction(Instruction.reg(Instruction.Type.ADDD, 9, 0, 1));
		for (int i=0; i<15; ++i) cpu.onTick();
		Logger.Debug("Add result (should be 4427173.3506) = " + cpu.getReg(12));		
		
		// Test multiplier throughput
		Logger.Debug("New Test...");
		cpu.addInstruction(Instruction.reg(Instruction.Type.MULD, 12, 0, 1));
		cpu.addInstruction(Instruction.reg(Instruction.Type.MULD, 13, 0, 1));
		cpu.addInstruction(Instruction.reg(Instruction.Type.MULD, 14, 0, 1));
		cpu.addInstruction(Instruction.reg(Instruction.Type.MULD, 15, 0, 1));
		cpu.addInstruction(Instruction.reg(Instruction.Type.MULD, 11, 0, 1));
		for (int i=0; i<45; ++i) cpu.onTick();
		
		// Test parallel execution of add and store
		Logger.Debug("New Test...");
		cpu.addInstruction(Instruction.reg(Instruction.Type.ADDD, 12, 0, 1));
		cpu.addInstruction(Instruction.reg(Instruction.Type.ADDD, 13, 0, 1));
		cpu.addInstruction(Instruction.mem(Instruction.Type.ST, 12, 0));
		cpu.addInstruction(Instruction.mem(Instruction.Type.ST, 13, 1));
		for (int i=0; i<10; ++i) cpu.onTick();
		Logger.Debug("Add result (should be 4214.45) = " + cpu.getMemory(0));
		Logger.Debug("Add result (should be 4214.45) = " + cpu.getMemory(1));
	}
	
	private OpQuene opQueue;
	private ArrayList<Component> components;
	private RegFile regFile;
	private Memory memory;
}
