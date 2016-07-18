package de.oninoni.OnionPower.Items;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Batrod {
	
	private static final String NAME = "§4Batrod";
	public static final int MAX_POWER = 64000;
	
	public static ItemStack create() {
		ItemStack batterod = new ItemStack(Material.BLAZE_ROD);
		
		ItemMeta itemMeta = batterod.getItemMeta();
		itemMeta.setDisplayName(NAME);
		List<String> lore = new ArrayList<>();
		lore.add("§h0");
		lore.add("§60/" + MAX_POWER + " " + CustomsItems.UNIT_NAME);
		lore.add("§h" + UUID.randomUUID());
		itemMeta.setLore(lore);
		batterod.setItemMeta(itemMeta);
		
		return batterod;
	}
	
	public static boolean check(ItemStack item) {
		if(item==null)return false;
		ItemMeta itemMeta = item.getItemMeta();
		if(itemMeta != null){
			if(itemMeta.getDisplayName() != null && itemMeta.getDisplayName().equalsIgnoreCase(NAME)){
				return true;
			}
		}
		return false;
	}
	
	public static void setPower(ItemStack item, int power){
		power = Math.min(power, MAX_POWER);
		ItemMeta itemMeta = item.getItemMeta();
		List<String> lore = itemMeta.getLore();
		lore.set(0, "§h" + power);
		lore.set(1, "§6" + power + "/" + MAX_POWER + " " + CustomsItems.UNIT_NAME);
		itemMeta.setLore(lore);
		item.setItemMeta(itemMeta);
	}
	
	public static int readPower(ItemStack item){
		return Integer.parseInt(item.getItemMeta().getLore().get(0).substring(2));
	}
}
