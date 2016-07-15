package de.oninoni.ElytraBooster.Listener;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CraftItemListener implements Listener{
	
	@EventHandler
	public void onCraft(PrepareItemCraftEvent e){
		if(e.getInventory().getResult().getItemMeta() == null)return;
		if(!(e.getInventory().getResult().getItemMeta().getLore() != null && e.getInventory().getResult().getItemMeta().getLore().size() >= 1))return;
		if(!e.getInventory().getResult().getItemMeta().getLore().get(0).equalsIgnoreCase("Fuel Level:"))return;
		ItemStack[] ingredients = e.getInventory().getMatrix();
		ItemStack sourceElytra = null;
		for(int i = 0; i < ingredients.length; i++){
			if(ingredients[i].getType() == Material.ELYTRA){
				sourceElytra = ingredients[i];
				break;
			}
		}
		if(sourceElytra == null)return;
		ItemMeta itemMeta = sourceElytra.getItemMeta();
		if(itemMeta == null)return;
		List<String> lore = itemMeta.getLore();
		if(lore == null)return;
		if(lore.size() != 2)return;
		String levelLine = lore.get(1);
		int level = 0;
		for(int i = 0; i < levelLine.length(); i++){
			if(levelLine.substring(i,i + 1).equals("%")){
				level = (int) (Float.parseFloat(levelLine.substring(0,i)) * 10);
				break;
			}
		}
		level += 125;
		level = Math.min(level, 1000);
		lore.set(1, level / 10.0 + "%");
		itemMeta.setLore(lore);
		sourceElytra.setItemMeta(itemMeta);
		e.getInventory().setResult(sourceElytra);
	}
}
