package de.oninoni.OnionPower.Listeners;

import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import de.oninoni.OnionPower.OnionPower;

public class EntityListener implements Listener{
	
	private static OnionPower plugin = OnionPower.get();
	
	public void onDeath(EntityDeathEvent e){
		plugin.getMachineManager().onEntityDeath(e);
	}
	
}
