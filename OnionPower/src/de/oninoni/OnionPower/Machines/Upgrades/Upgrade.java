package de.oninoni.OnionPower.Machines.Upgrades;

import org.bukkit.inventory.ItemStack;

import de.oninoni.OnionPower.Machines.Machine.UpgradeType;

public abstract class Upgrade {
	
	public abstract UpgradeType getType();
	
	public Upgrade(){
	}
	
	@Deprecated
	public static ItemStack getUpgrade(){
		return null;
	}
}