package de.oninoni.OnionPower.Items;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CustomsItems {
	public static final String UNIT_NAME = "OnionPower";
	
	public static ItemStack getGlassPane(byte metaData, String title){
		ItemStack pane = new ItemStack(Material.STAINED_GLASS_PANE, 1, metaData);
		ItemMeta meta = pane.getItemMeta();
		meta.setDisplayName(title);
		pane.setItemMeta(meta);
		return pane;
	}
	
	public static ItemStack getGlassPane(byte metaData, String title, List<String> lore){
		ItemStack pane = getGlassPane(metaData, title);
		ItemMeta meta = pane.getItemMeta();
		meta.setLore(lore);
		pane.setItemMeta(meta);
		return pane;
	}
	
	public static ItemStack getMinerPickAxe(){
		ItemStack pick = new ItemStack(Material.IRON_PICKAXE);
		ItemMeta meta = pick.getItemMeta();
		meta.setDisplayName("§4Internal Pickaxe");
		pick.setItemMeta(meta);
		return pick;
	}
}
