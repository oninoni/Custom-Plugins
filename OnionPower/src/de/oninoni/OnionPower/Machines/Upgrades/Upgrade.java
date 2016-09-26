package de.oninoni.OnionPower.Machines.Upgrades;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.oninoni.OnionPower.Machines.Machine.UpgradeType;

public abstract class Upgrade {
	
	public abstract UpgradeType getType();
	public abstract ItemStack getSettingsItem();
	public abstract ItemStack onClickSetting();
	
	public static String getName(UpgradeType type) {
		if(type == UpgradeType.RedstoneUpgrade){
			return "§6Upgrade: §4Redstone Upgrade";
		}
		return "§6Upgrade: §4";
	}
	
	public String getName(){
		return getName(getType());
	}
	
	public Upgrade(){
		
	}
	
	public static ItemStack getItem(UpgradeType type){
		ItemStack itemStack = new ItemStack(Material.PAPER);
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName(getName(type));
		itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		itemMeta.addEnchant(Enchantment.DURABILITY, 1, false);
		itemStack.setItemMeta(itemMeta);
		return itemStack;
	}
	
	public ItemStack getItem(){
		return getItem(getType());
	}
	
	public static boolean isUpgrade(ItemStack i, UpgradeType type){
		return i.getType() == Material.PAPER && i.getItemMeta().getDisplayName().startsWith(getName(type));
	}
	
	public static boolean isUpgrade(ItemStack i){
		return i.getType() == Material.PAPER && i.getItemMeta().getDisplayName().startsWith(getName(UpgradeType.Upgrade));
	}
}