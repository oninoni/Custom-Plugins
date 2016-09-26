package de.oninoni.OnionPower.Machines.Upgrades;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
	private Inventory inv;
	
	HashMap<Integer, Upgrade> upgrades;
	
	public UpgradeManager(Machine m){
		this(m, load(m));
	}
	
	private UpgradeManager(Machine m, HashMap<Integer, Upgrade> upgrades){
		this.upgrades = upgrades;
		machine = m;
		
		inv = plugin.getServer().createInventory((InventoryHolder) machine.getPosition().getBlock().getState(), 18, getName());
		for(int i = 0; i < 9; i++)inv.setItem(i + 9, CustomsItems.getGlassPane((byte) 15, "�4NO Upgrade in this Slot"));

		Set<Integer> keySet = upgrades.keySet();
		for (Integer key : keySet) {
			Upgrade u = upgrades.get(key);
			plugin.getLogger().info("Loaded: " + u.getName());
			ItemStack uItem = Upgrade.getItem(u.getType());
			inv.setItem(key, uItem);
			inv.setItem(key + 9, u.getSettingsItem());
		}
	}
	
	private static HashMap<Integer, Upgrade> load(Machine m){
		plugin.getLogger().info("Loading... Upgrades...");
		HashMap<Integer, Upgrade> loaded = new HashMap<>();
		
		List<String> lore = m.getPowerCore().getItemMeta().getLore();
		lore = lore.subList(5, lore.size());
		
		for (String line : lore) {
			String[] splitted = line.split("|");
			plugin.getLogger().info(line);
			if(splitted.length == 3){
				int id = Integer.parseInt(splitted[0]);
				String type = splitted[1];
				int value = Integer.parseInt(splitted[2]);
				if(UpgradeType.RedstoneUpgrade.toString() == type){
					loaded.put(id, new RedstoneUpgrade(value));
					plugin.getLogger().info(id + "|" + type + "|" + value);
				}
			}
		}
		
		plugin.getLogger().info("Loaded " + loaded.size() + " Upgrades");
		
		return loaded;
	}
	
	public List<String> getPowerCoreLore(){
		List<String> lore = new ArrayList<>();
		Set<Integer> keySet = upgrades.keySet();
		for (Integer key : keySet) {
			Upgrade u = upgrades.get(key);
			if(u instanceof RedstoneUpgrade){
				lore.add(key + "|" + u.getType().toString() + "|" + ((RedstoneUpgrade) u).getPowerSetting().ordinal());
			}
		}
		return lore;
	}
	
	public String getName(){
		return "�4Upgrades:";
	}
	
	public void openInterface(HumanEntity p){
		p.openInventory(inv);
	}
	
	public void updateIventories(){
		machine.setUpgradeLore();
		
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			@Override
			public void run() {
				for (HumanEntity viewer : inv.getViewers()) {
					((Player) viewer).updateInventory();
				}
			}
		}, 1L);
	}
	
	public void onClick(InventoryClickEvent e){
		if(e.getSlot() >= 0 && e.getSlot() <= 8){
			//Removing all Upgrades
			if(e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR){
				if(Upgrade.isUpgrade(e.getCurrentItem(), UpgradeType.RedstoneUpgrade)){
					upgrades.remove(e.getSlot());
					e.getInventory().setItem(e.getSlot() + 9, CustomsItems.getGlassPane((byte) 15, "�4NO Upgrade in this Slot"));
				}
			}
			//Adding all Upgrades
			if(e.getCursor() != null && e.getCursor().getType() != Material.AIR){
				if(Upgrade.isUpgrade(e.getCursor(), UpgradeType.RedstoneUpgrade)){
					//plugin.getLogger().info("Adding " + Upgrade.getName(UpgradeType.RedstoneUpgrade) + " to " + e.getSlot());
					RedstoneUpgrade upgrade = new RedstoneUpgrade();
					upgrades.put(e.getSlot(), upgrade);
					e.getInventory().setItem(e.getSlot() + 9, upgrade.getSettingsItem());
				}else{
					//plugin.getLogger().info("ABORT!" + e.getCursor());
					e.setCancelled(true);
				}
			}
			updateIventories();
		}else if(e.getSlot() >= 9 && e.getSlot() <= 17){
			//plugin.getLogger().info("Clicked in Slot: " + e.getSlot());
			if(e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR && upgrades.containsKey(e.getSlot() - 9)){
				Upgrade u = upgrades.get(e.getSlot() - 9);
				e.getInventory().setItem(e.getSlot(), u.onClickSetting());
				e.setCancelled(true);
			}else{
				e.getWhoClicked().getInventory().addItem(Upgrade.getItem(UpgradeType.RedstoneUpgrade));
				e.setCancelled(true);
			}
			updateIventories();
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
