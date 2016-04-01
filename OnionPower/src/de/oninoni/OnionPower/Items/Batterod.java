package de.oninoni.OnionPower.Items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Batterod {
	
	private static final String NAME = "&4Batterod";
	private static final int MAX_POWER = 64000;
	
	public static ItemStack create() {
		ItemStack batterod = new ItemStack(Material.BLAZE_ROD);
		
		ItemMeta itemMeta = batterod.getItemMeta();
		itemMeta.setDisplayName(NAME);
		List<String> lore = new ArrayList<>();
		lore.add("§h0");
		lore.add("§60/" + MAX_POWER + " " + CustomsItems.UNIT_NAME);
		itemMeta.setLore(lore);
		batterod.setItemMeta(itemMeta);
		
		return batterod;
	}
	
	public static boolean check(ItemStack item) {
		if(item==null)Bukkit.broadcastMessage("Possseidon Failed!");
		ItemMeta itemMeta = item.getItemMeta();
		if(itemMeta != null){
			if(itemMeta.getDisplayName().equalsIgnoreCase(NAME)){
				return true;
			}
		}
		return false;
	}
}
