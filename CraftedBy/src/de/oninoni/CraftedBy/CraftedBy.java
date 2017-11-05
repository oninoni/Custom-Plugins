package de.oninoni.CraftedBy;

import org.bukkit.plugin.java.JavaPlugin;

public class CraftedBy extends JavaPlugin{
	public void onEnable() {
		CraftingPrepareListener craftingPrepareListener = new CraftingPrepareListener();
		getServer().getPluginManager().registerEvents(craftingPrepareListener, this);
	}
}
