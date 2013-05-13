package tomasulo;

import java.util.ArrayList;

public class OpQuene {
	public OpQuene(ArrayList<Component> components)
	{
		this.components = components;	
		this.pendingQ = new ArrayList<Instruction>();
		this.runningQ = new ArrayList<Instruction>();
	}
	
	public void EnQueue(Instruction instruction)
	{
		pendingQ.add(instruction);
	}
	
	public void onTick()
	{
		this.tryIssue();
	}
	
	private void tryIssue()
	{		
		for (Instruction inst : this.pendingQ)
		{
			Logger.Debug("Trying to issue " + inst);
			for (Component component : this.components)
			{				
				if (component.ifAccept(inst) && !component.isFull())
				{
					component.accept(inst);
					this.pendingQ.remove(inst);
					this.runningQ.add(inst);
					
					return;
				}
			}
		}	
	}
	
	public ArrayList<Component> components;
	public ArrayList<Instruction> pendingQ;
	public ArrayList<Instruction> runningQ;
}
