package de.oninoni.OninoniUtil.Listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;

import de.oninoni.OninoniUtil.OninoniUtil;

public class PlayerKickBanListener implements Listener{
	
	private OninoniUtil plugin;
	
	public PlayerKickBanListener(OninoniUtil p) {
		plugin = p;
	}
	
	@EventHandler
	public void onKick(PlayerKickEvent e){
		Player player = e.getPlayer();
		String prefix = plugin.getPermissionsEx().getPermissionsManager().getUser(player).getPrefix();
		prefix = prefix.replace('&', '§');
		if(e.getPlayer().isBanned()){
			plugin.getServer().broadcastMessage(prefix + player.getName() + "§6 has been kicked from the Server!");
			if(e.getReason() != null){
				plugin.getServer().broadcastMessage("§6because: \"" + e.getReason() + "\"");
			}
		}else{
			plugin.getServer().broadcastMessage(prefix + player.getName() + "§6 has been banned from the Server!");
			if(e.getReason() != null){
				plugin.getServer().broadcastMessage("§6because: \"" + e.getReason() + "\"");
			}
		}
	}
	
}
