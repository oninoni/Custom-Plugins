package de.oninoni.OnionPower.Items.Statics;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ArcFurnaceCore {
	
	public static ItemStack create(Material type){
		ItemStack i = new ItemStack(type);
		ItemMeta itemMeta = i.getItemMeta();
		itemMeta.setDisplayName("§4Inside are: " + 0 + " Ores");
		i.setItemMeta(itemMeta);
		return i;
	}
	
	public static List<Long> getEnterTimes(ItemStack core){
		List<Long> times = new ArrayList<>();
		if(core == null) return new ArrayList<>();
		
		ItemMeta itemMeta = core.getItemMeta();
		List<String> lore = itemMeta.getLore();
		
		if(lore == null || lore.size() == 0) return new ArrayList<>();
		
		for (String string : lore) {
			times.add(Long.parseLong(string.substring(2)));
		}
		
		return times;
	}
	
	private static void setEnterTimes(ItemStack core, List<Long> times){
		if(core == null) return;
		//Bukkit.getLogger().info("Setting Times...");
		
		ItemMeta itemMeta = core.getItemMeta();
		List<String> lore = new ArrayList<>();
		
		for (Long time : times) {
			lore.add("§h" + time);
			//Bukkit.getLogger().info("Adding: " + time);
		}
		
		itemMeta.setLore(lore);
		
		itemMeta.setDisplayName("§4Inside are: " + times.size() + " Ores");
		
		core.setItemMeta(itemMeta);
	}
	
	public static void addTime(ItemStack core){
		long time = System.currentTimeMillis() / 50;
		List<Long> times = getEnterTimes(core);
		times.add(time);
		setEnterTimes(core, times);
	}
	
	public static void removeTime(ItemStack core, long time){
		List<Long> times = getEnterTimes(core);
		if(times.contains(time)){
			times.remove(time);
			setEnterTimes(core, times);
		}
	}
}
