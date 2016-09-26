package de.oninoni.OnionPower.Machines.Upgrades;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.oninoni.OnionPower.OnionPower;
import de.oninoni.OnionPower.Machines.Machine.UpgradeType;

public abstract class Upgrade {
	
	protected static OnionPower plugin = OnionPower.get();
	
	public abstract UpgradeType getType();
	public abstract ItemStack getSettingsItem();
	public abstract ItemStack onClickSetting();
	
	public static String getName(UpgradeType type) {
		switch (type) {
		case RedstoneUpgrade:
			return "§6Upgrade: §aRedstone";
		case RangeUpgrade:
			return "§6Upgrade: §aRange";
		default:
			return "§6Upgrade: §a";
		}
	}
	
	private static ArrayList<String> getLore(UpgradeType type){
		ArrayList<String> lore = new ArrayList<>();
		switch (type) {
		case RedstoneUpgrade:
			lore.add("§9This upgrade allows a Machine,");
			lore.add("§9to be controlled by §3Redstone§9.");
			break;
		case RangeUpgrade:
			lore.add("§9This can change the range");
			lore.add("§9of an §3Electrical Stripminer§9.");
			break;
		default:
			lore.add("§9Oninoni forgot to add a description here....");
		}
		return lore;
	}
	
	public String getName(){
		return getName(getType());
	}
	
	public static ItemStack getItem(UpgradeType type){
		ItemStack itemStack = new ItemStack(Material.PAPER);
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName(getName(type));
		itemMeta.setLore(getLore(type));
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