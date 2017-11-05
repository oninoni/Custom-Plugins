package de.oninoni.OnionPower.Machines.Upgrades;

import java.util.ArrayList;
import java.util.List;

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

	private static List<String> getLore(UpgradeType type) {
		List<String> lore = new ArrayList<String>();
		switch (type) {
		case Upgrade:
			lore.add("§9Serves as a base for all upgrades.");
			lore.add("§9Used in an §3Upgrade Station§9.");
			break;
		case RedstoneUpgrade:
			lore.add("§9Allows any §3Machine§9 to be");
			lore.add("§9controlled by §3Redstone§9.");
			break;
		case EfficiencyUpgrade:
			lore.add("§c[TODO]");
			break;
		case RangeUpgrade:
			lore.add("§9Allows you to increase or decrease");
			lore.add("§9the range of an §3Electrical Stripminer§9.");
			break;
		case LavaUpgrade:
			lore.add("§9Allows storage of Lava, when");
			lore.add("§9placed inside of a §3Fluid Handler§9.");
			break;
		default:
			lore.add("§cMissing Lore");
		}
		return lore;
	}

	public static String getName(UpgradeType type) {
		if (type == UpgradeType.Upgrade)
		{
			return "§6Upgrade-Base";
		}
		else
		{
			String name;
			switch (type) {
			case RedstoneUpgrade:
				name = "§aRedstone";
				break;
			case EfficiencyUpgrade:
				name = "§aEfficency";
				break;
			case RangeUpgrade:
				name = "§aRange";
				break;
			case LavaUpgrade:
				name = "§aLava";
				break;
			default:
				name = "§cMissing Name";
			}
			return "§6Upgrade: " + name;
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