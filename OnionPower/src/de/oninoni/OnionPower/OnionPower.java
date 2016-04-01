package de.oninoni.OnionPower;

import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

public class OnionPower extends JavaPlugin{
	
	private ProtocolManager protocolManager;
	public ProtocolLibManager protocolLibManager;
	
	public static OnionPower get(){return JavaPlugin.getPlugin(OnionPower.class);}
	
	public void onEnable() {
		protocolManager = ProtocolLibrary.getProtocolManager();
		protocolLibManager = new ProtocolLibManager(protocolManager);
		protocolLibManager.addLoreListener();
		
	}
	
	public void onDisable() {
		
	}
	
}
