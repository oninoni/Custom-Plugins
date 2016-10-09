package de.oninoni.OnionPower.Items.PowerItems;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Batrod extends PowerItem{
	private static final String NAME = "§4Batrod";
	
	public Batrod(int power){
		super(Material.BLAZE_ROD, 1, (short) 0, NAME, power);
	}
	
	public Batrod(ItemStack item){
		super(item, NAME);
	}
}