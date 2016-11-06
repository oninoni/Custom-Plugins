package de.oninoni.OnionPower.Items.Statics;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class UraniumBuffer {
	
	public static ItemStack create(){
		ItemStack hopper = new ItemStack(Material.HOPPER);
		ItemMeta itemMeta = hopper.getItemMeta();
		
		itemMeta.setDisplayName("§4Uranium - Buffer");
		
		List<String> lore = new ArrayList<>();
		lore.add(0, "§h0");
		lore.add(1, "§60/512g Uranium");
		
		itemMeta.setLore(lore);
		hopper.setItemMeta(itemMeta);
		return hopper;
	}
	
	public static int getLevel(ItemStack item){
		ItemMeta itemMeta = item.getItemMeta();
		List<String> lore = itemMeta.getLore();
		String data = lore.get(0);
		return Integer.parseInt(data.substring(2));
	}
	
	public static boolean addLevel(ItemStack item){
		int level = getLevel(item);
		level++;
		setLevel(item, level);
		if(level == 512) return true;
		return false;
	}
	
	public static void setLevel(ItemStack item, int level){
		ItemMeta itemMeta = item.getItemMeta();
		List<String> lore = itemMeta.getLore();
		lore.set(0, "§h" + level);
		lore.set(1, "§6" + level + "/512g Uranium");
		itemMeta.setLore(lore);
		item.setItemMeta(itemMeta);
	}
	
}
