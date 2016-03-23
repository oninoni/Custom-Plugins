package de.oninoni.OninoniUtil.Listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import de.oninoni.OninoniUtil.OninoniUtil;

public class PlayerJoinListener implements Listener{
	
	OninoniUtil plugin;
	
	public PlayerJoinListener(OninoniUtil p) {
		plugin = p;
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event){
		String color = plugin.getPermissionsEx().getPermissionsManager().getUser(event.getPlayer()).getPrefix();
		color = color.replace('&', '§');
		event.getPlayer().setPlayerListName(color + event.getPlayer().getName());
	}
	
}
