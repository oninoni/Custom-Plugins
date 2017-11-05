package de.oninoni.OnionPower.Items.PowerItems.PowerTools;

import org.bukkit.Material;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

import de.oninoni.OnionPower.Items.PowerItems.Batrod;
import de.oninoni.OnionPower.Items.PowerItems.PowerItem;

public class PowerTool extends PowerItem{

	short initialDurability = -1;
	int initialBatrodPower = -1;
	
	public PowerTool(int amount, short damage, String name, int power){
		super(amount, damage, name, power);
	}
	
	public PowerTool(ItemStack item, String name) {
		super(item, name);
	}
	
	@Override
	public void onCraft(PrepareItemCraftEvent e) {
		
		ItemStack[] items = e.getInventory().getStorageContents();
		
		//Skipping 0 cause 0 is Result for some Reason
		for (int i = 1; i < items.length; i++) {
			ItemStack item = items[i];
			if(item.getType() == Material.AIR)continue;
			Batrod batrod = new Batrod(item);
			if(batrod.check()){
				initialBatrodPower = batrod.getPower();
			}else{
				PowerItem powerItem = new PowerItem(item);
				//plugin.getLogger().info(item+"");
				if(!powerItem.check()){
					initialDurability = batrod.getDurability();
				}
			}
		}
		
		if(initialBatrodPower == -1 || initialDurability == -1){
			e.getInventory().setResult(new ItemStack(Material.AIR));
		}
	}

}
