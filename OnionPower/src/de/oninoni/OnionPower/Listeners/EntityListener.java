package de.oninoni.OnionPower.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityListener implements Listener{
	
	//private static OnionPower plugin = OnionPower.get();
	
	@EventHandler
	public void onDeath(EntityDamageEvent e){
		//plugin.getMachineManager().onEntityDeath(e);
		//TODO Respawn Entities when they get kill doh!
	}
}