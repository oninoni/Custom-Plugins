package de.oninoni.OnionPower.Items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.oninoni.OnionPower.Machines.Machine;

public class PowerCore {
	
	private static final String NAME = "§4Power Core";
	
	public static ItemStack create(Machine m){
		ItemStack powerCore = new ItemStack(Material.END_CRYSTAL);
		
		ItemMeta itemMeta = powerCore.getItemMeta();
		itemMeta.setDisplayName(NAME);
		List<String> lore = new ArrayList<>();
		lore.add("§6" + m.getDisplayName() + " Power:");
		lore.add("§6" + m.getPower() + " / " + m.getMaxPower() + " " + CustomsItems.UNIT_NAME);
		itemMeta.setLore(lore);
		powerCore.setItemMeta(itemMeta);
		
		return powerCore;
	}
	
	public static boolean check(ItemStack item){
		if(item==null)Bukkit.broadcastMessage("Possseidon Failed!");
		ItemMeta itemMeta = item.getItemMeta();
		if(itemMeta != null){
			if(itemMeta.getDisplayName().equalsIgnoreCase(NAME)){
				return true;
			}
		}
		return false;
	}
	
	public static void setPowerLevel(ItemStack powerCore, int power, int maxPower){
		ItemMeta itemMeta = powerCore.getItemMeta();
		List<String> lore = itemMeta.getLore();
		lore.set(1, "§6" + power + " / " + maxPower + " " + CustomsItems.UNIT_NAME);
		itemMeta.setLore(lore);
		powerCore.setItemMeta(itemMeta);
	}
}
