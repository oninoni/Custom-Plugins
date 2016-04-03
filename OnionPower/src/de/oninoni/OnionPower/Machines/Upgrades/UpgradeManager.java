package de.oninoni.OnionPower.Machines.Upgrades;

import java.util.HashMap;

import de.oninoni.OnionPower.Machines.Machine;

public class UpgradeManager {
	
	private Machine machine;
	
	HashMap<Integer, MachineUpgrade> upgrades;
	
	public UpgradeManager(Machine m){
		upgrades = new HashMap<>();
		machine = m;
	}
	
	public UpgradeManager(Machine m, HashMap<Integer, MachineUpgrade> upgrades){
		this.upgrades = upgrades;
		machine = m;
	}
}
