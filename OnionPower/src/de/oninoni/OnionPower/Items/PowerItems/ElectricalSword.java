package de.oninoni.OnionPower.Items.PowerItems;

import org.bukkit.Material;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

public class ElectricalSword extends PowerTool{
	private static final String NAME = "§4Electrical Sword";
	
	public ElectricalSword(int power, short damage){
		super(Material.GOLD_SWORD, 1, damage, NAME, power);
	}
	
	public ElectricalSword(ItemStack item){
		super(item, NAME);
	}
	
	@Override
	public void onCraft(PrepareItemCraftEvent e) {
		super.onCraft(e);
		if(initialBatrodPower != -1 && initialDurability != -1)
			e.getInventory().setResult(new ElectricalSword(initialBatrodPower, initialDurability));
	}
}
