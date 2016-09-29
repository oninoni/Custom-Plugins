package de.oninoni.Minions.Listener;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;

public class EntityListener implements Listener {
	
	//private static Minions plugin = Minions.get();
	
	@EventHandler
	private void onTarget(EntityTargetEvent e)
	{
		if (e.getEntityType() == EntityType.ZOMBIE && !(e.getTarget() instanceof ArmorStand))
			e.setCancelled(true);
	}

}
