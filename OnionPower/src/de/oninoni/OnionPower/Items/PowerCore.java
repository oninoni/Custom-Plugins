package de.oninoni.OnionPower.Items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.oninoni.OnionPower.Machines.Machine;

public class PowerCore {
	
	//private static OnionPower plugin = OnionPower.get();
	
	private static final String NAME = "§4Power Core";
	
	public static ItemStack create(Machine m){
		ItemStack powerCore = new ItemStack(Material.END_CRYSTAL);
		
		ItemMeta itemMeta = powerCore.getItemMeta();
		itemMeta.setDisplayName(NAME);
		List<String> lore = new ArrayList<>();
		lore.add(0, "§6" + m.getDisplayName() + " Power:");
		lore.add(1, "§6" + m.getPower() + " / " + m.getMaxPower() + " " + CustomsItems.UNIT_NAME);
		if(m.getMaxPowerOutput() == 0){
			lore.add(2, "§6Using: " + m.getPowerOutputTotal() + " " + CustomsItems.UNIT_NAME);
		}else{
			lore.add(2, "§6Output: " + m.getPowerOutputTotal() + " / " + m.getMaxPowerOutput() + " " + CustomsItems.UNIT_NAME);
		}
		if(m.getMaxPowerInput() == 0){
			lore.add(3, "§6Generating: " + m.getPowerIntputTotal() + " " + CustomsItems.UNIT_NAME);
		}else{
			lore.add(3, "§6Input: " + m.getPowerIntputTotal() + " / " + m.getMaxPowerInput() + " " + CustomsItems.UNIT_NAME);
		}
		lore.add(4, "§h" + m.getPower());
		itemMeta.setLore(lore);
		powerCore.setItemMeta(itemMeta);
		
		return powerCore;
	}
	
	public static boolean check(ItemStack item){
		if(item==null)return false;
		ItemMeta itemMeta = item.getItemMeta();
		if(itemMeta != null){
			if(itemMeta.getDisplayName().equalsIgnoreCase(NAME)){
				return true;
			}
		}
		return false;
	}
	
	public static void setPowerLevel(ItemStack powerCore, Machine m){
		//plugin.getLogger().info("UPPDATED Power Core: " + m.getPower());
		if(powerCore==null || m==null)return;
		ItemMeta itemMeta = powerCore.getItemMeta();
		List<String> lore = itemMeta.getLore();
		if(lore.size() < 5)return;
		lore.set(1, "§6" + m.getPower() + " / " + m.getMaxPower() + " " + CustomsItems.UNIT_NAME);
		if(m.getMaxPowerOutput() == 0){
			lore.set(2, "§6Using: " + m.getPowerOutputTotal() + " " + CustomsItems.UNIT_NAME);
		}else{
			lore.set(2, "§6Output: " + m.getPowerOutputTotal() + " / " + m.getMaxPowerOutput() + " " + CustomsItems.UNIT_NAME);
		}
		if(m.getMaxPowerInput() == 0){
			lore.set(3, "§6Generating: " + m.getPowerIntputTotal() + " " + CustomsItems.UNIT_NAME);
		}else{
			lore.set(3, "§6Input: " + m.getPowerIntputTotal() + " / " + m.getMaxPowerInput() + " " + CustomsItems.UNIT_NAME);
		}
		lore.set(4, "§h" + m.getPower());
		itemMeta.setLore(lore);
		powerCore.setItemMeta(itemMeta);
	}
	
	public static int getPowerLevel(ItemStack powerCore){
		if(powerCore==null)return -1;
		return Integer.parseInt(powerCore.getItemMeta().getLore().get(4).substring(2));
	}
}
