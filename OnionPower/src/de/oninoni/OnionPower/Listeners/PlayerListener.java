package de.oninoni.OnionPower.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import de.oninoni.OnionPower.OnionPower;
import de.oninoni.OnionPower.Items.PowerItems.Batrod;

public class PlayerListener implements Listener {

	private static OnionPower plugin = OnionPower.get();

	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		plugin.getMachineManager().onMove(e);
	}
	
	@EventHandler
	public void onPrepareCrafting(PrepareItemCraftEvent e){
		if(e.getInventory().getResult() instanceof Batrod){
			e.getInventory().setResult(new Batrod());
		}
	}

}
