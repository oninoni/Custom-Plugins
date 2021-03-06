package de.oninoni.OnionPower.Items.PowerItems.PowerTools;

import org.bukkit.Material;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

public class ElectricalHoe extends PowerTool{
	private static final String NAME = "�4Electrical Hoe";
	
	public ElectricalHoe(int power, short damage){
		super(1, damage, NAME, power);
	}
	
	public ElectricalHoe(ItemStack item){
		super(item, NAME);
	}
	
	@Override
	protected Material getOriginalType() {
		return Material.DIAMOND_HOE;
	}
	
	@Override
	public void onCraft(PrepareItemCraftEvent e) {
		super.onCraft(e);
		if(initialBatrodPower != -1 && initialDurability != -1)
			e.getInventory().setResult(new ElectricalHoe(initialBatrodPower, initialDurability));
	}
	
	@Override
	public Material getVisibleType() {
		return Material.GOLD_HOE;
	}
}
