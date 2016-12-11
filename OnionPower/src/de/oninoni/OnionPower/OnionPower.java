package de.oninoni.OnionPower;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

import de.oninoni.OnionPower.Items.PowerItems.Batrod;
import de.oninoni.OnionPower.Items.PowerItems.PowerTools.ElectricalAxe;
import de.oninoni.OnionPower.Items.PowerItems.PowerTools.ElectricalBoots;
import de.oninoni.OnionPower.Items.PowerItems.PowerTools.ElectricalChestplate;
import de.oninoni.OnionPower.Items.PowerItems.PowerTools.ElectricalElytra;
import de.oninoni.OnionPower.Items.PowerItems.PowerTools.ElectricalHelmet;
import de.oninoni.OnionPower.Items.PowerItems.PowerTools.ElectricalHoe;
import de.oninoni.OnionPower.Items.PowerItems.PowerTools.ElectricalJetlytra;
import de.oninoni.OnionPower.Items.PowerItems.PowerTools.ElectricalLeggings;
import de.oninoni.OnionPower.Items.PowerItems.PowerTools.ElectricalPickaxe;
import de.oninoni.OnionPower.Items.PowerItems.PowerTools.ElectricalShovel;
import de.oninoni.OnionPower.Items.PowerItems.PowerTools.ElectricalSword;
import de.oninoni.OnionPower.Items.Statics.CraftingRecipes;
import de.oninoni.OnionPower.Items.TutorialBook.TutorialBook;
import de.oninoni.OnionPower.Listeners.BlockBreakListener;
import de.oninoni.OnionPower.Listeners.ChunkListener;
import de.oninoni.OnionPower.Listeners.EntityListener;
import de.oninoni.OnionPower.Listeners.InventoryListener;
import de.oninoni.OnionPower.Listeners.PlayerListener;
import de.oninoni.OnionPower.Machines.MachineManager;
import de.oninoni.OnionPower.Machines.Upgrades.Upgrade;
import de.oninoni.OnionPower.Machines.Upgrades.UpgradeManager.UpgradeType;
import de.oninoni.OnionPower.NMS.NMSAdapter;
import de.oninoni.OnionPower.NMS.NMSAdapter_1_10;
import de.oninoni.OnionPower.NMS.NMSAdapter_1_11;
import de.oninoni.OnionPower.NMS.NMSAdapter_1_9;

public class OnionPower extends JavaPlugin {

	public static OnionPower get() {
		return JavaPlugin.getPlugin(OnionPower.class);
	}

	public static String getMinecraftVersion() {
		String s = Bukkit.getBukkitVersion();
		return s;
	}

	private ProtocolManager protocolManager;

	private ProtocolLibManager protocolLibManager;

	private MachineManager machineManager;
	
	private NMSAdapter nmsAdapter;

	public MachineManager getMachineManager() {
		return machineManager;
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (command.getName().equalsIgnoreCase("poweritems") && sender.isOp()) {
			if (sender instanceof Player) {
				Inventory inv = Bukkit.createInventory((Player) sender, 27, "§4Powered Items");
				
				inv.setItem(0 , new Batrod(0));
				inv.setItem(1 , new Batrod(640000));
				
				inv.setItem(9 , new ElectricalAxe(640000, (short) 0));
				inv.setItem(10, new ElectricalPickaxe(640000, (short) 0));
				inv.setItem(11, new ElectricalShovel(640000, (short) 0));
				inv.setItem(12, new ElectricalHoe(640000, (short) 0));
				
				inv.setItem(17, new ElectricalSword(640000, (short) 0));
				
				inv.setItem(18, new ElectricalHelmet(640000, (short) 0));
				inv.setItem(19, new ElectricalChestplate(640000, (short) 0));
				inv.setItem(20, new ElectricalLeggings(640000, (short) 0));
				inv.setItem(21, new ElectricalBoots(640000, (short) 0));

				inv.setItem(24, new TutorialBook());
				
				inv.setItem(25, new ElectricalJetlytra(640000, (short) 0));
				inv.setItem(26, new ElectricalElytra(640000, (short) 0));
				
				((Player) sender).openInventory(inv);
				return true;
			}
		}else if(command.getName().equalsIgnoreCase("upgrades") && sender.isOp()){
			if(sender instanceof Player){
				Inventory inv = Bukkit.createInventory((Player) sender, 27, "§4Powered Items");
				 
				inv.setItem(0, Upgrade.getItem(UpgradeType.RedstoneUpgrade));
				inv.setItem(1, Upgrade.getItem(UpgradeType.RangeUpgrade));
				inv.setItem(2, Upgrade.getItem(UpgradeType.LavaUpgrade));
				
				((Player) sender).openInventory(inv);
				return true;
			}
		}
		return false;
	}

	public void onDisable() {
		machineManager.saveData();
		machineManager.killAllNames();
	}

	public void onEnable() {
		getLogger().info(getMinecraftVersion());

		if (getMinecraftVersion().equals("1.9.4-R0.1-SNAPSHOT")) {
			getLogger().info("1.9.4 Mode Activated!");
			nmsAdapter = new NMSAdapter_1_9();
		} else if (getMinecraftVersion().equals("1.10.2-R0.1-SNAPSHOT")) {
			getLogger().info("1.10.2 Mode Activated!");
			nmsAdapter = new NMSAdapter_1_10();
		} else if (getMinecraftVersion().equals("1.11-R0.1-SNAPSHOT")){
			getLogger().info("1.11 Mode Activated!");
			nmsAdapter = new NMSAdapter_1_11();
		}
		
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
		
		CraftingRecipes.setAllRecipes();
	}
	
	public NMSAdapter getNMSAdapter(){
		return nmsAdapter;
	}
}
