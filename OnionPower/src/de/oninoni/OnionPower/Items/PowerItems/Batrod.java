package de.oninoni.OnionPower.Items.PowerItems;

import org.bukkit.Material;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

public class Batrod extends PowerItem{
	private static final String NAME = "§4Batrod";
	
	public Batrod(int power){
		super(1, (short) 0, NAME, power);
	}
	
	public Batrod(ItemStack item){
		super(item, NAME);
	}

	@Override
	protected Material getOriginalType() {
		return Material.BLAZE_ROD;
	}
	
	@Override
	public void onCraft(PrepareItemCraftEvent e) {
		e.getInventory().setResult(new Batrod(0));
	}
}