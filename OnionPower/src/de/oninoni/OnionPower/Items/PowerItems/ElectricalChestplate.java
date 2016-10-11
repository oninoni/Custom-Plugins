package de.oninoni.OnionPower.Items.PowerItems;

import org.bukkit.Material;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

public class ElectricalChestplate extends PowerTool{
	private static final String NAME = "§4Electrical Chestplate";
	
	public ElectricalChestplate(int power, short damage){
		super(Material.DIAMOND_CHESTPLATE, 1, damage, NAME, power);
	}
	
	public ElectricalChestplate(ItemStack item){
		super(item, NAME);
	}
	
	@Override
	public void onCraft(PrepareItemCraftEvent e) {
		super.onCraft(e);
		if(initialBatrodPower != -1 && initialDurability != -1)
			e.getInventory().setResult(new ElectricalChestplate(initialBatrodPower, initialDurability));
	}
	
	@Override
	public Material getVisibleType() {
		return Material.GOLD_CHESTPLATE;
	}
}
