package de.oninoni.OnionPower.Items.Statics;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import de.oninoni.OnionPower.OnionPower;
import de.oninoni.OnionPower.Items.PowerItems.Batrod;
import de.oninoni.OnionPower.Items.PowerItems.PowerTools.ElectricalAxe;
import de.oninoni.OnionPower.Items.PowerItems.PowerTools.ElectricalBoots;
import de.oninoni.OnionPower.Items.PowerItems.PowerTools.ElectricalChestplate;
import de.oninoni.OnionPower.Items.PowerItems.PowerTools.ElectricalElytra;
import de.oninoni.OnionPower.Items.PowerItems.PowerTools.ElectricalHelmet;
import de.oninoni.OnionPower.Items.PowerItems.PowerTools.ElectricalHoe;
import de.oninoni.OnionPower.Items.PowerItems.PowerTools.ElectricalLeggings;
import de.oninoni.OnionPower.Items.PowerItems.PowerTools.ElectricalPickaxe;
import de.oninoni.OnionPower.Items.PowerItems.PowerTools.ElectricalShovel;
import de.oninoni.OnionPower.Items.PowerItems.PowerTools.ElectricalSword;
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
		
		ShapelessRecipe sr = new ShapelessRecipe(new ElectricalAxe(0, (short) 0));
		
		sr.addIngredient(Material.BLAZE_ROD);
		sr.addIngredient(Material.GOLD_AXE);
		
		Bukkit.addRecipe(sr);
		
		sr = new ShapelessRecipe(new ElectricalHoe(0, (short) 0));
		
		sr.addIngredient(Material.BLAZE_ROD);
		sr.addIngredient(Material.GOLD_HOE);
		
		Bukkit.addRecipe(sr);
		
		sr = new ShapelessRecipe(new ElectricalPickaxe(0, (short) 0));
		
		sr.addIngredient(Material.BLAZE_ROD);
		sr.addIngredient(Material.GOLD_PICKAXE);
		
		Bukkit.addRecipe(sr);
		
		sr = new ShapelessRecipe(new ElectricalShovel(0, (short) 0));
		
		sr.addIngredient(Material.BLAZE_ROD);
		sr.addIngredient(Material.GOLD_SPADE);
		
		Bukkit.addRecipe(sr);
		
		sr = new ShapelessRecipe(new ElectricalSword(0, (short) 0));
		
		sr.addIngredient(Material.BLAZE_ROD);
		sr.addIngredient(Material.GOLD_SWORD);
		
		Bukkit.addRecipe(sr);
		
		sr = new ShapelessRecipe(new ElectricalHelmet(0, (short) 0));
		
		sr.addIngredient(Material.BLAZE_ROD);
		sr.addIngredient(Material.GOLD_HELMET);
		
		Bukkit.addRecipe(sr);
		
		sr = new ShapelessRecipe(new ElectricalChestplate(0, (short) 0));
		
		sr.addIngredient(Material.BLAZE_ROD);
		sr.addIngredient(Material.GOLD_CHESTPLATE);
		
		Bukkit.addRecipe(sr);
		
		sr = new ShapelessRecipe(new ElectricalLeggings(0, (short) 0));
		
		sr.addIngredient(Material.BLAZE_ROD);
		sr.addIngredient(Material.GOLD_LEGGINGS);
		
		Bukkit.addRecipe(sr);
		
		sr = new ShapelessRecipe(new ElectricalBoots(0, (short) 0));
		
		sr.addIngredient(Material.BLAZE_ROD);
		sr.addIngredient(Material.GOLD_BOOTS);
		
		Bukkit.addRecipe(sr);
		
		sr = new ShapelessRecipe(new ElectricalElytra(0, (short) 0));
		
		sr.addIngredient(Material.BLAZE_ROD);
		sr.addIngredient(Material.ELYTRA);
		
		Bukkit.addRecipe(sr);
	}
}
