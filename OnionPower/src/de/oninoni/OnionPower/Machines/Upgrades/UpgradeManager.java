package de.oninoni.OnionPower.Machines.Upgrades;

import java.util.HashMap;

public class UpgradeManager {
	
	HashMap<Integer, MachineUpgrade> upgrades;
	
	public UpgradeManager(){
		upgrades = new HashMap<>();
	}
	
	public UpgradeManager(HashMap<Integer, MachineUpgrade> upgrades){
		this.upgrades = upgrades;
	}
	
}
