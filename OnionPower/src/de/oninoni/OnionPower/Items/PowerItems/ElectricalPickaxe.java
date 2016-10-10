package de.oninoni.OnionPower.Items.PowerItems;

import org.bukkit.Material;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

public class ElectricalPickaxe extends PowerTool{
	private static final String NAME = "§4Electrical Pickaxe";
	
	public ElectricalPickaxe(int power, short damage){
		super(Material.GOLD_PICKAXE, 1, damage, NAME, power);
	}
	
	public ElectricalPickaxe(ItemStack item){
		super(item, NAME);
	}
	
	@Override
	public void onCraft(PrepareItemCraftEvent e) {
		super.onCraft(e);
		if(initialBatrodPower != -1 && initialDurability != -1)
			e.getInventory().setResult(new ElectricalPickaxe(initialBatrodPower, initialDurability));
	}
}
