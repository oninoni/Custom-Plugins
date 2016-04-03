package de.oninoni.OnionPower.Machines.Upgrades;

import org.bukkit.inventory.ItemStack;

import de.oninoni.OnionPower.Machines.MachineManager;

public abstract class MachineUpgrade {
	
	MachineManager machineManager;
	
	public MachineUpgrade(MachineManager mM){
		machineManager = mM;
	}
	
	@Deprecated
	public static ItemStack getUpgrade(){
		return null;
	}
}
