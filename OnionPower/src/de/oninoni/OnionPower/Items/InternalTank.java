package de.oninoni.OnionPower.Items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InternalTank {
	
	public static ItemStack create(int level, int maxLevel, boolean lava){
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
	
	//TODO Read Write For Tank
}
