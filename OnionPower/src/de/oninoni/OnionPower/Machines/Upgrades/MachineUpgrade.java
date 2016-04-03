package de.oninoni.OnionPower.Machines.Upgrades;

import de.oninoni.OnionPower.Machines.MachineManager;

public abstract class MachineUpgrade {
	
	MachineManager machineManager;
	
	public MachineUpgrade(MachineManager mM){
		machineManager = mM;
	}
	
}
