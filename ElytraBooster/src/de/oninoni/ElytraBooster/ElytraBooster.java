package de.oninoni.ElytraBooster;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import de.oninoni.ElytraBooster.Listener.CraftItemListener;
import de.oninoni.ElytraBooster.Listener.PlayerMoveListener;

public class ElytraBooster extends JavaPlugin{
	
	public ItemStack getBoosterElytra(String charge){
		ItemStack item = new ItemStack(Material.ELYTRA);
		ItemMeta itemMeta = item.getItemMeta();
		ArrayList<String> lore = new ArrayList<>();
		lore.add("Fuel Level:");
		lore.add(charge + "%");
		itemMeta.setLore(lore);
		itemMeta.setDisplayName("Jetlytra");
		item.setItemMeta(itemMeta);
		
		return item;
	}
	
	public ItemStack getBoosterElytra(int charge){
		return getBoosterElytra(charge+"");
	}
	
	@SuppressWarnings("deprecation")
	private void addRechargeRecipe(){
		final ShapelessRecipe recipe = new ShapelessRecipe(getBoosterElytra("+12.5"));
		recipe.addIngredient(Material.ELYTRA, -1);
		for(int i = 0; i<8; i++){
			recipe.addIngredient(Material.COAL);
		}
		getServer().addRecipe(recipe);
	}
	
	private void addCraftingRecipe(){
		final ShapedRecipe recipe = new ShapedRecipe(getBoosterElytra(0));
		recipe.shape("RSR", "RER", "R0R");
		recipe.setIngredient('E', Material.ELYTRA);
		recipe.setIngredient('R', Material.FIREWORK);
		recipe.setIngredient('S', Material.NETHER_STAR);
		
		getServer().addRecipe(recipe);
	}
	
	public void onEnable() {
		PlayerMoveListener playerToggleFlightListener = new PlayerMoveListener();
		CraftItemListener craftItemListener = new CraftItemListener();
		getServer().getPluginManager().registerEvents(playerToggleFlightListener, this);
		getServer().getPluginManager().registerEvents(craftItemListener, this);
		
		addCraftingRecipe();
		addRechargeRecipe();
	}
	
	public void onDisable(){
		
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player){
			if(command.getName().equalsIgnoreCase("elytra")){
				Player p = (Player) sender;
				p.getInventory().addItem(getBoosterElytra(100));
			}
		}
		return false;
	}
	
}
