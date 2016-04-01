package de.oninoni.OnionPower;

import org.bukkit.plugin.java.JavaPlugin;

public class OnionPower extends JavaPlugin{
	
	public ProtocolLibManager protocolLibManager = new ProtocolLibManager();
	
	public static OnionPower get(){return JavaPlugin.getPlugin(OnionPower.class);}
	
	public void onEnable() {
		protocolLibManager.addLoreListener();
	}
	
	public void onDisable() {
		
	}
	
}
