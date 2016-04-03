package de.oninoni.MiniGames;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;

public class MiniGames extends JavaPlugin {
	
	public static MiniGames get(){return JavaPlugin.getPlugin(MiniGames.class);}
	
	private WorldEditPlugin worldEdit;
	
	private GameManager gameManager;
	
	public void onEnable() {
		worldEdit = (WorldEditPlugin) getServer().getPluginManager().getPlugin("WorldEdit");
		if(worldEdit != null){
			getLogger().info("World Edit detected!");
		}else{
			getLogger().warning("World Edit missing!");
		}
		
		gameManager = new GameManager();
	}
	
	public void onDisable() {
		
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		return false;
	}
	
	public WorldEditPlugin getWorldEdit() {
		return worldEdit;
	}
	
	public GameManager getGameManager() {
		return gameManager;
	}
}
