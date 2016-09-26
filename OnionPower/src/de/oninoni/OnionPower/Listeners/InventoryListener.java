package de.oninoni.OnionPower.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;

import de.oninoni.OnionPower.OnionPower;

public class InventoryListener implements Listener{
	
	private static OnionPower plugin = OnionPower.get();
	
	@EventHandler
	public void onClick(InventoryClickEvent e){
		plugin.getMachineManager().onClick(e);
	}
	
	@EventHandler
	public void onClose(InventoryCloseEvent e){
		plugin.getMachineManager().onClose(e);
	}
	
	@EventHandler
	public void onMove(InventoryMoveItemEvent e){
		plugin.getMachineManager().onMove(e);
	}
	
	@EventHandler
	public void onDrag(InventoryDragEvent e){
		plugin.getMachineManager().onDrag(e);
	}
	
	@EventHandler
	public void onDispense(BlockDispenseEvent e){
		plugin.getMachineManager().onDispense(e);
	}
}
