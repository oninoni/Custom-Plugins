package de.oninoni.OnionPower.Machines.Upgrades;

import java.util.ArrayList;

import org.bukkit.inventory.ItemStack;

import de.oninoni.OnionPower.Items.CustomsItems;
import de.oninoni.OnionPower.Machines.Machine.UpgradeType;

public class RangeUpgrade extends Upgrade{

	int range;
	
	public RangeUpgrade(){
		this(5);
	}
	
	public RangeUpgrade(int value){
		range = value;
	}
	
	@Override
	public UpgradeType getType() {
		return UpgradeType.RangeUpgrade;
	}

	public int getRange(){
		return (int)Math.pow(2, range);
	}
	
	@Override
	public ItemStack getSettingsItem() {
		ArrayList<String> lore = new ArrayList<>();
		if(range == 0){
			lore.add("§41 Block");
		}else{
			lore.add("§4" + getRange() + " Blocks");
		}
		
		ItemStack settingsItem = CustomsItems.getGlassPane((byte) 6, "§6Range will be limited to:", lore);
		return settingsItem;
	}

	@Override
	public ItemStack onClickSetting() {
		range = (range + 1) % 8;
		return getSettingsItem();
	}
	
	
	
}
