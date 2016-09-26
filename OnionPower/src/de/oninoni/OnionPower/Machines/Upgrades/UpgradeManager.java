package de.oninoni.OnionPower.Machines.Upgrades;

import java.util.HashMap;
import java.util.Set;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import de.oninoni.OnionPower.OnionPower;
import de.oninoni.OnionPower.Machines.Machine;

public class UpgradeManager {
	
	protected static OnionPower plugin = OnionPower.get();
	
	private Machine machine;
	
	HashMap<Integer, Upgrade> upgrades;
	
	public UpgradeManager(Machine m){
		this(m, new HashMap<>());
	}
	
	public UpgradeManager(Machine m, HashMap<Integer, Upgrade> upgrades){
		this.upgrades = upgrades;
		machine = m;
	}
	
	public String getName(){
		return "§4Upgrades:";
	}
	
	public void openInterface(HumanEntity p){
		Inventory inv = plugin.getServer().createInventory((InventoryHolder) machine.getPosition().getBlock().getState(), 18, getName());
		
		Set<Integer> keySet = upgrades.keySet();
		for (Integer key : keySet) {
			Upgrade u = upgrades.get(key);
			@SuppressWarnings("static-access")
			ItemStack uItem = u.getItem();
			inv.setItem(key, uItem);
		}
		
		p.openInventory(inv);
	}
	
	public void onClick(InventoryClickEvent e){
		if(e.getSlot() >= 0 && e.getSlot() <= 8){
			//Adding all Upgrades
			if(UpgradeRedstone.isUpgrade(e.getCursor())){
				upgrades.put(e.getSlot(), new UpgradeRedstone());
			}else{
				e.setCancelled(true);
			}
			//Removing all Upgrades
			if(UpgradeRedstone.isUpgrade(e.getCurrentItem())){
				upgrades.remove(e.getSlot());
			}else{
				e.setCancelled(true);
			}
		}else if(e.getSlot() >= 9 && e.getSlot() <= 17){
			//TODO Settings
		}else{
			plugin.getLogger().warning("Upgrade Slot: " + e.getSlot() + " selected!");
		}
	}
}
