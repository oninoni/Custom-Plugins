package de.oninoni.OnionPower.Items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Uranium extends ItemStack{
	
	public Uranium(){
		super(Material.EMERALD);
		ItemMeta itemMeta = getItemMeta();
		itemMeta.setDisplayName("§4Uranium");
		setItemMeta(itemMeta);
	}
	
	@Override
	public boolean equals(Object obj) {
		return super.equals(obj) && getItemMeta().getDisplayName().equals("§4Uranium");
	}
}
