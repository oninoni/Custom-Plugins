package de.oninoni.OnionPower;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

import de.oninoni.OnionPower.Items.Batrod;
import de.oninoni.OnionPower.Listeners.BlockBreakListener;
import de.oninoni.OnionPower.Listeners.ChunkListener;
import de.oninoni.OnionPower.Listeners.EntityListener;
import de.oninoni.OnionPower.Listeners.InventoryListener;
import de.oninoni.OnionPower.Listeners.PlayerListener;
import de.oninoni.OnionPower.Machines.MachineManager;

public class OnionPower extends JavaPlugin {

	private ProtocolManager protocolManager;
	private ProtocolLibManager protocolLibManager;

	private MachineManager machineManager;

	public static OnionPower get() {
		return JavaPlugin.getPlugin(OnionPower.class);
	}

	public void onEnable() {
		protocolManager = ProtocolLibrary.getProtocolManager();
		protocolLibManager = new ProtocolLibManager(protocolManager);
		protocolLibManager.addLoreListener();

		InventoryListener inventoryListener = new InventoryListener();
		getServer().getPluginManager().registerEvents(inventoryListener, this);

		BlockBreakListener blockBreakListener = new BlockBreakListener();
		getServer().getPluginManager().registerEvents(blockBreakListener, this);

		ChunkListener chunkListener = new ChunkListener();
		getServer().getPluginManager().registerEvents(chunkListener, this);

		PlayerListener playerListener = new PlayerListener();
		getServer().getPluginManager().registerEvents(playerListener, this);

		EntityListener entityListener = new EntityListener();
		getServer().getPluginManager().registerEvents(entityListener, this);

		machineManager = new MachineManager();

		Bukkit.getScheduler().runTaskLater(this, new Runnable() {
			private OnionPower plugin = OnionPower.get();

			@Override
			public void run() {
				Bukkit.getScheduler().runTaskLater(plugin, this, 2L);
				machineManager.update();
			}
		}, 2L);

		Bukkit.getScheduler().runTaskLater(this, new Runnable() {
			private OnionPower plugin = OnionPower.get();

			@Override
			public void run() {
				Bukkit.getScheduler().runTaskLater(plugin, this, 20L * 60L);
				machineManager.saveData();
			}
		}, 20L * 60L);

		machineManager.loadData();
	}

	public void onDisable() {
		machineManager.saveData();
		machineManager.killAllNames();
	}

	public MachineManager getMachineManager() {
		return machineManager;
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (command.getName().equalsIgnoreCase("batrod")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				player.getInventory().addItem(Batrod.create());
				return true;
			}
		}
		return false;
	}
}
