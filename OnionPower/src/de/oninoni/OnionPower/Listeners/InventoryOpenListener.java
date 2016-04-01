package de.oninoni.OnionPower.Listeners;

import org.bukkit.block.Furnace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.InventoryHolder;

import de.oninoni.OnionPower.NMSAdapter;

public class InventoryOpenListener implements Listener {
	
	@EventHandler
	public void onOpen(InventoryOpenEvent e) {
		InventoryHolder ih = e.getView().getTopInventory().getHolder();
		if (ih instanceof Furnace){
			NMSAdapter.setInvNameFurnace((Furnace) ih, e.getPlayer().getName());
		}
	}
	
}
