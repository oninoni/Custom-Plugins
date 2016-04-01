package de.oninoni.OnionPower.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

import de.oninoni.OnionPower.OnionPower;

public class InventoryListener implements Listener{
	
	private static OnionPower plugin = OnionPower.get();
	
	@EventHandler
	public void onClick(InventoryClickEvent e){
		plugin.getMachineManager().onClick(e);
	}

	@EventHandler
	public void onOpen(InventoryOpenEvent e) {
		plugin.getMachineManager().onOpen(e);
	}
	
	@EventHandler
	public void onMove(InventoryMoveItemEvent e){
		
	}
}
