package de.oninoni.OnionPower.Machines.Upgrades;

import java.util.HashMap;

import de.oninoni.OnionPower.Machines.Machine;

public class UpgradeManager {
	
	private Machine machine;
	
	HashMap<Integer, Upgrade> upgrades;
	
	public UpgradeManager(Machine m, HashMap<Integer, Upgrade> upgrades){
		this.upgrades = upgrades;
		machine = m;
	}
}
