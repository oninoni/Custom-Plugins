package de.oninoni.OnionPower;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

import de.oninoni.OnionPower.Listeners.InventoryOpenListener;

public class OnionPower extends JavaPlugin {
	
	private ProtocolManager protocolManager;
	public ProtocolLibManager protocolLibManager;
	
	public static OnionPower get(){return JavaPlugin.getPlugin(OnionPower.class);}
	
	public void onEnable() {
		protocolManager = ProtocolLibrary.getProtocolManager();
		protocolLibManager = new ProtocolLibManager(protocolManager);
		protocolLibManager.addLoreListener();
		
		InventoryOpenListener inventoryOpenListener = new InventoryOpenListener();
		getServer().getPluginManager().registerEvents(inventoryOpenListener, this);
		
		Bukkit.getScheduler().runTaskLater(this, new Runnable() {
			private OnionPower plugin = OnionPower.get();
			
			@Override
			public void run() {
				
				
				Bukkit.getScheduler().runTaskLater(plugin, this, 5L);
			}
		}, 5L);
	}
	
	public void onDisable() {
		
	}
}
