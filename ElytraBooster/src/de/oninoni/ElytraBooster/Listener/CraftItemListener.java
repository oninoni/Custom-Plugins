package de.oninoni.ElytraBooster.Listener;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CraftItemListener implements Listener{
	
	@EventHandler
	public void onCraft(CraftItemEvent event){
		if(!(event.getInventory().getResult().getItemMeta() == null))return;
		if(!event.getInventory().getResult().getItemMeta().getLore().get(0).equalsIgnoreCase("Fuel Level:"))return;
		ItemStack[] ingredients = event.getInventory().getMatrix();
		ItemStack elytra = new ItemStack(Material.AIR);
		for(int i = 0; i < ingredients.length; i++){
			if(ingredients[i].getType() == Material.ELYTRA){
				elytra = ingredients[i];
				break;
			}
		}
		ItemMeta itemMeta = elytra.getItemMeta();
		List<String> lore = itemMeta.getLore();
		if(lore.size() != 2)return;
		String levelLine = lore.get(1);
		int level = 0;
		for(int i = 0; i < levelLine.length(); i++){
			if(levelLine.substring(i,i + 1).equals("%")){
				level = (int) (Float.parseFloat(levelLine.substring(0,i)) * 10);
				break;
			}
		}
		if(level >= 1000){
			event.getInventory().setResult(new ItemStack(Material.AIR));
			event.setCancelled(true);
			return;
		}
		level+=125;
		lore.set(1, level / 10.0+"%");
		itemMeta.setLore(lore);
		elytra.setItemMeta(itemMeta);
		event.getInventory().setResult(elytra);
	}
}
