package de.oninoni.OnionPower.Items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ArkHeater {
	
	public static ItemStack create(int heat){
		ItemStack i = new ItemStack(Material.MAGMA);
		ItemMeta itemMeta = i.getItemMeta();
		itemMeta.setDisplayName("§4Heater");
		List<String> lore = new ArrayList<>();
		lore.add(0, "§h" + heat);
		lore.add(1, "§6" + heat + " / " + 1000 + "°C");
		itemMeta.setLore(lore);
		i.setItemMeta(itemMeta);
		return i;
	}
	
	public static int readHeat(ItemStack heater){
		if(heater == null || heater.getType() != Material.MAGMA) return 0;
		
		ItemMeta itemMeta = heater.getItemMeta();
		List<String> lore = itemMeta.getLore();
		
		if(lore == null || lore.size() != 2) return 0;
		
		return Integer.parseInt(lore.get(0).substring(2));
	}
	
	public static void setHeat(ItemStack heater, int heat){
		if(heater == null || heater.getType() != Material.MAGMA) return;
		
		ItemMeta itemMeta = heater.getItemMeta();
		List<String> lore = itemMeta.getLore();
		
		if(lore == null || lore.size() != 2) return;
		
		lore.set(0, "§h" + heat);
		lore.set(1, "§6" + heat + " / " + 1000 + "°C");
		
		itemMeta.setLore(lore);
		heater.setItemMeta(itemMeta);
	}

}
