package de.oninoni.Minions;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import de.oninoni.Minions.Listener.EntityListener;
import de.oninoni.Minions.Listener.PlayerListener;

public class Minions extends JavaPlugin 
{
	
	private ArrayList<Zombie> zombies; 
	private ArmorStand target;
	
	public static Minions get()
	{
		return JavaPlugin.getPlugin(Minions.class);
	}
	
	@Override
	public void onEnable() 
	{
		
		PlayerListener playerListener = new PlayerListener();
		EntityListener entityListener = new EntityListener();
		
		getServer().getPluginManager().registerEvents(playerListener, this);
		getServer().getPluginManager().registerEvents(entityListener, this);
		
		zombies = new ArrayList<>();
		
	}
	
	@Override
	public void onDisable() 
	{	

	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) 
	{
		if (!(sender instanceof Player))
		{
			sender.sendMessage("§cOnly Players can use this command!");
			return true;
		}
		
		Player player = (Player) sender;
		
		if (label.equalsIgnoreCase("minion"))
		{
			Zombie zombie = (Zombie) player.getWorld().spawnEntity(player.getLocation(), EntityType.ZOMBIE);
			zombie.setBaby(true);
			EntityEquipment equipment = zombie.getEquipment();
			equipment.setItemInMainHand(new ItemStack(Material.IRON_PICKAXE));
			zombies.add(zombie);
			return true;
		} 
		else if (label.equalsIgnoreCase("comehere"))
		{
			for (Zombie zombie : zombies)
			{
				target = (ArmorStand) player.getWorld().spawnEntity(player.getLocation(), EntityType.ARMOR_STAND);
				zombie.setTarget(target);
			}
			return true;
		}
		return false;
	}
	
}
