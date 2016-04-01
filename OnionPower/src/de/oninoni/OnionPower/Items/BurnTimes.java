package de.oninoni.OnionPower.Items;

import java.util.HashMap;

import org.bukkit.Material;

public class BurnTimes {
	
	public static final HashMap<Material, Short> Item;
	
	static {
		Item = new HashMap<>();
		Item.put(Material.COAL, (short)42);
		Item.put(Material.WOOD, (short)12);
	}
	
}
