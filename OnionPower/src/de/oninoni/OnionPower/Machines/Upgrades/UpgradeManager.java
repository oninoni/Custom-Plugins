package de.oninoni.OnionPower.Machines.Upgrades;

import java.util.HashMap;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import de.oninoni.OnionPower.OnionPower;
import de.oninoni.OnionPower.Machines.Machine;

public class UpgradeManager {
	
	protected static OnionPower plugin = OnionPower.get();
	
	private Machine machine;
	
	HashMap<Integer, Upgrade> upgrades;
	
	public UpgradeManager(Machine m, HashMap<Integer, Upgrade> upgrades){
		this.upgrades = upgrades;
		machine = m;
	}
	
	public String getName(){
		return "§4Upgrades:";
	}
	
	public void openInterface(HumanEntity p){
		Inventory inv = plugin.getServer().createInventory((InventoryHolder) machine.getPosition().getBlock().getState(), 9, getName());
		p.openInventory(inv);
	}
	
	public void onClick(InventoryClickEvent e){
		plugin.getLogger().info("Click");
	}
}
