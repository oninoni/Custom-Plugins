package de.oninoni.OnionPower.Items;

import java.util.HashMap;
import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.Recipe;

public class ItemData {
	
	public static final HashMap<Material, Short> burnTime;
	
	public static final HashMap<Material, Material> smeltable;
	
	static void initBurnTimes() {
		burnTime.put(Material.LAVA, 			(short)20000 );
		burnTime.put(Material.COAL_BLOCK, 		(short)16000);
		burnTime.put(Material.BLAZE_ROD, 		(short)2400);
		burnTime.put(Material.COAL, 			(short)1600);
		burnTime.put(Material.LOG, 				(short)300);
		burnTime.put(Material.LOG_2, 			(short)300);
		burnTime.put(Material.WOOD, 			(short)300);
		burnTime.put(Material.WOOD_PLATE, 		(short)300);
		burnTime.put(Material.FENCE, 			(short)300);
		burnTime.put(Material.WOOD_STAIRS, 		(short)300);
		burnTime.put(Material.TRAP_DOOR, 		(short)300);
		burnTime.put(Material.WORKBENCH, 		(short)300);
		burnTime.put(Material.BOOKSHELF, 		(short)300);
		burnTime.put(Material.CHEST, 			(short)300);
		burnTime.put(Material.TRAPPED_CHEST, 	(short)300);
		burnTime.put(Material.DAYLIGHT_DETECTOR,(short)300);
		burnTime.put(Material.JUKEBOX, 			(short)300);
		burnTime.put(Material.NOTE_BLOCK, 		(short)300);
		burnTime.put(Material.HUGE_MUSHROOM_1, 	(short)300);
		burnTime.put(Material.HUGE_MUSHROOM_2, 	(short)300);
		burnTime.put(Material.BANNER, 			(short)300);
		burnTime.put(Material.WOOD_PICKAXE, 	(short)200);
		burnTime.put(Material.WOOD_SPADE, 		(short)200);
		burnTime.put(Material.WOOD_HOE, 		(short)200);
		burnTime.put(Material.WOOD_SWORD, 		(short)200);
		burnTime.put(Material.WOOD_AXE, 		(short)200);
		burnTime.put(Material.WOOD_STEP, 		(short)150);
		burnTime.put(Material.SAPLING,		 	(short)100);
		burnTime.put(Material.STICK, 			(short)100);
	}
	
	static void initBurnable() {
		Iterator<Recipe> recipeIterator = Bukkit.recipeIterator();
		while(recipeIterator.hasNext()){
			Recipe recipe = recipeIterator.next();
			if(recipe instanceof FurnaceRecipe){
				FurnaceRecipe furnaceRecipe = ((FurnaceRecipe) recipe);
				smeltable.put(furnaceRecipe.getInput().getType(), furnaceRecipe.getResult().getType());
			}
		}
	}
	
	static {
		burnTime = new HashMap<>();
		initBurnTimes();
		smeltable = new HashMap<>();
		initBurnable();
	}	
	
}
