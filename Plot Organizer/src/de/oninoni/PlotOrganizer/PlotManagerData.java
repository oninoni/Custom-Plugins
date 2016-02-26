package de.oninoni.PlotOrganizer;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class PlotManagerData {
	private static String FILENAME = "plotdata.yml"; 
	
	private FileConfiguration plotDataConfig = null;
	private File plotDataConfigFile = null;
	
	private PlotOrganizer plugin;
	
	public PlotManagerData(PlotOrganizer pl){
		this.plugin = pl;
	}

	public void reloadConfig() {
	    if (plotDataConfigFile == null) {
	    	plotDataConfigFile = new File(plugin.getDataFolder(), FILENAME);
	    }
	    plotDataConfig = YamlConfiguration.loadConfiguration(plotDataConfigFile);
	}
	
	public FileConfiguration getConfig() {
	    if (plotDataConfig == null) {
	        reloadConfig();
	    }
	    return plotDataConfig;
	}
	
	public void saveConfig() {
	    if (plotDataConfig == null || plotDataConfigFile == null) {
	    	return;
	    }
	    try {
	        plotDataConfig.save(plotDataConfigFile);
	    } catch (IOException ex) {
	        Logger.getLogger(JavaPlugin.class.getName()).log(Level.SEVERE, "Konfiguration konnte nicht nach " + plotDataConfigFile + " geschrieben werden.", ex);
	    }
	}
	
}
