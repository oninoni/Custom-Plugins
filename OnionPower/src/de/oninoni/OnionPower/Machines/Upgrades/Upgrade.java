package de.oninoni.OnionPower.Machines.Upgrades;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.oninoni.OnionPower.Machines.Machine.UpgradeType;

public abstract class Upgrade {
	
	public abstract UpgradeType getType();
	
	public static String getName(){
		return "§6Upgrade: §5";
	}
	
	public Upgrade(){
		
	}
	
	public static ItemStack getItem(){
		ItemStack itemStack = new ItemStack(Material.PAPER);
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName(getName());
		itemStack.setItemMeta(itemMeta);
		return itemStack;
	}
	
	public static boolean isUpgrade(ItemStack i){
		return i.getType() == Material.PAPER && i.getItemMeta().getDisplayName().startsWith(getName());
	}
}