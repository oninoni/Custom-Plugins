package de.oninoni.CraftedBy;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CraftingPrepareListener implements Listener{
	
	@EventHandler
	public void onCraftingPrepare(PrepareItemCraftEvent e){
		ItemStack result = e.getInventory().getResult();
		
		ItemMeta itemMeta = result.getItemMeta();
		List<String> lore = itemMeta.getLore();
		if(lore == null) lore = new ArrayList<>();
		
		Player player = (Player) e.getViewers().get(0);
		
		lore.add("§4crafted by " + player.getName());
		
		itemMeta.setLore(lore);
		result.setItemMeta(itemMeta);
		e.getInventory().setResult(result);
	}
}
