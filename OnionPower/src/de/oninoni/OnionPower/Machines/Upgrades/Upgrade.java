package de.oninoni.OnionPower.Machines.Upgrades;

import org.bukkit.inventory.ItemStack;

import de.oninoni.OnionPower.Machines.MachineManager;

public abstract class Upgrade {
	
	MachineManager machineManager;
	
	public Upgrade(MachineManager mM){
		machineManager = mM;
	}
	
	@Deprecated
	public static ItemStack getUpgrade(){
		return null;
	}
}