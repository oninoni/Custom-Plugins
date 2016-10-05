package de.oninoni.OnionPower.Items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.oninoni.OnionPower.OnionPower;
import de.oninoni.OnionPower.Machines.HopperBased.FluidHandler;

public class InternalTank {
	protected static OnionPower plugin = OnionPower.get();

	public static ItemStack create(int level, int maxLevel, boolean lava) {
		ItemStack tank = new ItemStack(Material.STAINED_GLASS, 1, lava ? (short) 1 : (short) 3);
		ItemMeta meta = tank.getItemMeta();
		meta.setDisplayName("§9" + (lava ? "Lava" : "Water") + " Tank");
		List<String> lore = new ArrayList<>();
		lore.add(0, "§9" + level + " / " + maxLevel + " Buckets of " + (lava ? "Lava" : "Water"));
		lore.add(1, "§h" + level);
		meta.setLore(lore);
		tank.setItemMeta(meta);
		return tank;
	}

	public static int readTankLevel(ItemStack tank) {
		if (tank == null || tank.getItemMeta().getLore() == null)
			return 0;
		List<String> lore = tank.getItemMeta().getLore();
		return Integer.parseInt(lore.get(1).substring(2));
	}

	public static void setTankLevel(ItemStack tank, FluidHandler fH) {
		if (tank == null || tank.getItemMeta().getLore() == null)
			return;
		ItemMeta itemMeta = tank.getItemMeta();
		List<String> lore = itemMeta.getLore();
		lore.set(0, "§9" + fH.getLevel() + " / " + fH.getMaxLevel() + " Buckets of "
				+ (fH.isLavaMode() ? "Lava" : "Water"));
		lore.set(1, "§h" + fH.getLevel());
		itemMeta.setLore(lore);
		tank.setItemMeta(itemMeta);
	}

	@SuppressWarnings("deprecation")
	public static ItemStack setLavaMode(FluidHandler fH) {
		return create(fH.getLevel(), fH.getMaxLevel(), fH.isLavaMode());
	}

	public static boolean getLavaMode(ItemStack tank) {
		String name = tank.getItemMeta().getDisplayName();
		if (name.contains("Lava")) {
			return true;
		} else {
			return false;
		}
	}
}
