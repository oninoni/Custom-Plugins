package de.oninoni.OnionPower.Items;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ShapedRecipe;

import de.oninoni.OnionPower.OnionPower;
import de.oninoni.OnionPower.Items.PowerItems.Batrod;
import de.oninoni.OnionPower.Machines.Upgrades.Upgrade;
import de.oninoni.OnionPower.Machines.Upgrades.UpgradeManager.UpgradeType;

public class CraftingRecipes {
	protected static OnionPower plugin = OnionPower.get();

	public static void setAllRecipes(){
		ShapedRecipe r = new ShapedRecipe(new Batrod(0));

		r.shape("RRR",
				"GBG",
				"RRR");
		
		r.setIngredient('B', Material.BLAZE_ROD);
		r.setIngredient('G', Material.GOLD_INGOT);
		r.setIngredient('R', Material.REDSTONE);
		
		Bukkit.addRecipe(r);
		
		r = new ShapedRecipe(Upgrade.getItem(UpgradeType.Upgrade));
		
		r.shape("RPR",
				"PIP",
				"PPP");
		
		r.setIngredient('P', Material.PAPER);
		r.setIngredient('I', Material.IRON_INGOT);
		r.setIngredient('R', Material.REDSTONE);
		
		Bukkit.addRecipe(r);
	}
}
