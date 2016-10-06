package de.oninoni.OnionPower.Machines.Upgrades;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.oninoni.OnionPower.OnionPower;
import de.oninoni.OnionPower.Machines.Machine;
import de.oninoni.OnionPower.Machines.Upgrades.UpgradeManager.UpgradeType;

public abstract class Upgrade {

	protected static OnionPower plugin = OnionPower.get();

	public static ItemStack getItem(UpgradeType type) {
		ItemStack itemStack = new ItemStack(Material.PAPER);
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName(getName(type));
		itemMeta.setLore(getLore(type));
		itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		itemMeta.addEnchant(Enchantment.DURABILITY, 1, false);
		itemStack.setItemMeta(itemMeta);
		return itemStack;
	}

	private static ArrayList<String> getLore(UpgradeType type) {
		ArrayList<String> lore = new ArrayList<>();
		switch (type) {
		case RedstoneUpgrade:
			lore.add("§9This upgrade allows a §3Machine§9,");
			lore.add("§9to be controlled by §3Redstone§9.");
			break;
		case RangeUpgrade:
			lore.add("§9This can change the range");
			lore.add("§9of an §3Electrical Stripminer§9.");
			break;
		case LavaUpgrade:
			lore.add("§9If placed inside a §3Fluid");
			lore.add("§3Handler§9 this will make it");
			lore.add("§3lava.");
			break;
		default:
			lore.add("§9Basic Upgrade. Can be modified");
			lore.add("§9in the §3Upgrade Station");
		}
		return lore;
	}

	public static String getName(UpgradeType type) {
		switch (type) {
		case RedstoneUpgrade:
			return "§6Upgrade: §aRedstone";
		case RangeUpgrade:
			return "§6Upgrade: §aRange";
		case LavaUpgrade:
			return "§6Upgrade: §aLava";
		default:
			return "§6Upgrade: §a";
		}
	}

	public static boolean isUpgrade(ItemStack i) {
		return isUpgrade(i, UpgradeType.Upgrade);
	}

	public static boolean isUpgrade(ItemStack i, UpgradeType type) {
		return i != null && i.getType() == Material.PAPER
				&& i.getItemMeta().getDisplayName() != null && i.getItemMeta().getDisplayName().startsWith(getName(type));
	}

	protected Machine machine;

	public Upgrade(Machine m) {
		machine = m;
	}

	public ItemStack getItem() {
		return getItem(getType());
	}

	public String getName() {
		return getName(getType());
	}

	public abstract ItemStack getSettingsItem();

	public abstract UpgradeType getType();

	public abstract ItemStack onClickSetting();
}