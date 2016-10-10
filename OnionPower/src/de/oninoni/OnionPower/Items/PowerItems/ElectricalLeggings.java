package de.oninoni.OnionPower.Items.PowerItems;

import org.bukkit.Material;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

public class ElectricalLeggings extends PowerTool{
	private static final String NAME = "§4Electrical Leggings";
	
	public ElectricalLeggings(int power, short damage){
		super(Material.GOLD_LEGGINGS, 1, damage, NAME, power);
	}
	
	public ElectricalLeggings(ItemStack item){
		super(item, NAME);
	}
	
	@Override
	public void onCraft(PrepareItemCraftEvent e) {
		super.onCraft(e);
		plugin.getLogger().info(initialBatrodPower + "/" + initialDurability);
		if(initialBatrodPower != -1 && initialDurability != -1)
			e.getInventory().setResult(new ElectricalLeggings(initialBatrodPower, initialDurability));
	}
}
