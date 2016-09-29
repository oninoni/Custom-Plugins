package de.oninoni.OnionPower.Items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import de.oninoni.OnionPower.Machines.HopperBased.FluidHandler;

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
	
	public static int readTankLevel(ItemStack tank){
		if(tank == null || tank.getItemMeta().getLore() == null)return 0;
		List<String> lore = tank.getItemMeta().getLore();
		return Integer.parseInt(lore.get(1).substring(2));
	}
	
	public static void setTankLevel(ItemStack tank, FluidHandler fH){
		if(tank == null || tank.getItemMeta().getLore() == null)return;
		ItemMeta itemMeta = tank.getItemMeta();
		List<String> lore = itemMeta.getLore();
		lore.set(0, "§9" + fH.getFluid() + " / " + fH.getMaxFluid() + " Buckets of " + (fH.isLavaMode() ? "Lava" : "Water"));
		lore.set(1, "§h" + fH.getFluid());
		itemMeta.setLore(lore);
		tank.setItemMeta(itemMeta);
	}
	
	@SuppressWarnings("deprecation")
	public static boolean setLavaMode(ItemStack tank, FluidHandler fH){
		if(readTankLevel(tank) == 0){
			MaterialData data = tank.getData();
			data.setData((byte) (fH.isLavaMode() ? (short) 1 : (short) 3));
			tank.setData(data);
			ItemMeta itemMeta = tank.getItemMeta();
			itemMeta.setDisplayName("§9" + (fH.isLavaMode() ? "Lava" : "Water") + " Tank");
			tank.setItemMeta(itemMeta);
			setTankLevel(tank, fH);
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean getLavaMode(ItemStack tank){
		String name = tank.getItemMeta().getDisplayName();
		if(name.contains("Lava")){
			return true;
		}else{
			return false;
		}
	}
}
