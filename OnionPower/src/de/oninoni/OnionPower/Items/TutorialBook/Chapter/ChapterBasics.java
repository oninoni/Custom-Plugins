package de.oninoni.OnionPower.Items.TutorialBook.Chapter;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;

import de.oninoni.OnionPower.Items.TutorialBook.BookRecipes;

public class ChapterBasics extends Chapter{
	@Override
	public List<String> getChapter() {
		List<String> chapter = new ArrayList<>();
		
		chapter.add(
				"{\"text\":\"§4§lBasics:§r§0\n\n\""
				+ ",\"extra\":["
					+ "{\"text\":\""
					+ "This mod add a unit of §4§lelectrical power§r§0 to minecraft.\n"
					+ "You will be able to use this power to your advantage and create §4§lMachines§r§0 and §4§lTools§r§0 to aid you in your adventure.\n"
					+ "\"}"
				+ "]}"
		);
		
		chapter.add(
				"{\"text\":\"§4§lBatrod:§r§0\n\n\""
				+ ",\"extra\":["
					+ "{\"text\":\""
					+ "The §4§lBatrod§r§0\nstores energy and is the base ingredient for all machines!\n"
					+ "\n"
					+ "Recipe:\n\n\"},"
					+ BookRecipes.createRecipe(new Material[]{
							Material.REDSTONE,	 	Material.REDSTONE, 		Material.REDSTONE,
							Material.GOLD_INGOT, 	Material.BLAZE_ROD,		Material.GOLD_INGOT,
							Material.REDSTONE,		Material.REDSTONE, 		Material.REDSTONE, })
				+ "]}"
		);
		
		return chapter;
	}
}
