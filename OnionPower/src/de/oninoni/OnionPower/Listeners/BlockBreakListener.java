package de.oninoni.OnionPower.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import de.oninoni.OnionPower.OnionPower;

public class BlockBreakListener implements Listener{
	
	private static OnionPower plugin = OnionPower.get();
	
	@EventHandler
	public void onBreak(BlockBreakEvent e){
		plugin.getMachineManager().onBreak(e);
	}
	
}
