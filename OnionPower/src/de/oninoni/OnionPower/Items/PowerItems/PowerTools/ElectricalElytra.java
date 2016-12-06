package de.oninoni.OnionPower.Items.PowerItems.PowerTools;

import org.bukkit.Material;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

public class ElectricalElytra extends PowerTools{
	private static final String NAME = "§4Electrical Elytra";
	
	public ElectricalElytra(int power, short damage){
		super(1, damage, NAME, power);
	}
	
	public ElectricalElytra(ItemStack item){
		super(item, NAME);
	}
	
	@Override
	protected Material getOriginalType() {
		return Material.ELYTRA;
	}
	
	@Override
	public Material getVisibleType() {
		return Material.FEATHER;
	}
	
	@Override
	public void onCraft(PrepareItemCraftEvent e) {
		super.onCraft(e);
		if(initialBatrodPower != -1 && initialDurability != -1)
			e.getInventory().setResult(new ElectricalElytra(initialBatrodPower, initialDurability));
	}
}
