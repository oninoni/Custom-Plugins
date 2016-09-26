package de.oninoni.OnionPower.Machines.Upgrades;

import java.util.HashMap;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import de.oninoni.OnionPower.OnionPower;
import de.oninoni.OnionPower.Items.CustomsItems;
import de.oninoni.OnionPower.Machines.Machine;
import de.oninoni.OnionPower.Machines.Machine.UpgradeType;

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
			plugin.getLogger().info("Loaded: " + u.getName());
			ItemStack uItem = Upgrade.getItem(u.getType());
			inv.setItem(key, uItem);
			inv.setItem(key + 9, u.getSettingsItem());
		}
		
		p.openInventory(inv);
	}
	
	public void onClick(InventoryClickEvent e){
		if(e.getSlot() >= 0 && e.getSlot() <= 8){
			//Removing all Upgrades
			if(e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR){
				if(Upgrade.isUpgrade(e.getCurrentItem(), UpgradeType.RedstoneUpgrade)){
					upgrades.remove(e.getSlot());
					e.getInventory().setItem(e.getSlot() + 9, CustomsItems.getGlassPane((byte) 15, "&4NO Upgrade in this Slot"));
				}
			}
			//Adding all Upgrades
			if(e.getCursor() != null && e.getCursor().getType() != Material.AIR){
				if(Upgrade.isUpgrade(e.getCursor(), UpgradeType.RedstoneUpgrade)){
					//plugin.getLogger().info("Adding " + Upgrade.getName(UpgradeType.RedstoneUpgrade) + " to " + e.getSlot());
					UpgradeRedstone upgrade = new UpgradeRedstone();
					upgrades.put(e.getSlot(), upgrade);
					e.getInventory().setItem(e.getSlot() + 9, upgrade.getSettingsItem());
				}else{
					//plugin.getLogger().info("ABORT!" + e.getCursor());
					e.setCancelled(true);
				}
			}
			Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
				@Override
				public void run() {
					for (HumanEntity viewer : e.getInventory().getViewers()) {
						((Player) viewer).updateInventory();
					}
				}
			}, 1L);
		}else if(e.getSlot() >= 9 && e.getSlot() <= 17){
			//plugin.getLogger().info("Clicked in Slot: " + e.getSlot());
			if(e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR){
				Upgrade u = upgrades.get(e.getSlot() - 9);
				e.getInventory().setItem(e.getSlot(), u.onClickSetting());
				e.setCancelled(true);
			}else{
				e.getWhoClicked().getInventory().addItem(Upgrade.getItem(UpgradeType.RedstoneUpgrade));
			}
		}else{
			plugin.getLogger().warning("Upgrade Slot: " + e.getSlot() + " selected!");
		}
	}
	
	public Upgrade getUpgrade(UpgradeType type){
		Set<Integer> keySet = upgrades.keySet();
		for (Integer key : keySet) {
			Upgrade u = upgrades.get(key);
			if(u.getType() == type){
				return u;
			}
		}
		return null;
	}
}
