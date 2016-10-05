package de.oninoni.OnionPower.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import de.oninoni.OnionPower.OnionPower;

public class ChunkListener implements Listener {

	private static OnionPower plugin = OnionPower.get();

	@EventHandler
	public void onLoad(ChunkLoadEvent e) {
		plugin.getMachineManager().onLoad(e);
	}

	@EventHandler
	public void onUnload(ChunkUnloadEvent e) {
		plugin.getMachineManager().onUnload(e);
	}
}
