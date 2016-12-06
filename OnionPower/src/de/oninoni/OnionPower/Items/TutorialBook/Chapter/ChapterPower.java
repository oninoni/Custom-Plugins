package de.oninoni.OnionPower.Items.TutorialBook.Chapter;

import java.util.ArrayList;
import java.util.List;

public class ChapterPower extends Chapter {
	@Override
	public List<String> getChapter() {
		List<String> chapter = new ArrayList<>();
		
		chapter.add(""
				+ "§4§lPower:§r§0\n"
				+ "\n"
				+ "This chapter handles the basic ways of §4§lPower Generation§r§0.\n"
				+ "\n"
				+ "Bigger generators are in the Multiblock chapter!"
		);
		
		chapter.add("§4§lCables:§r§0\n"
				+ "\n"
				+ "To bring power to your machines you have to use\n§5§liron bars§r§0\nas cables."
		);
		
		chapter.add(""
				+ "§4§lGenerator:§r§0\n"
				+ "\n"
				+ "The §4§lGenerator§r§0 \nuses normal fuel to create some power from the heat.\n"
				+ "\n"
				+ "The §5§ltop slot§r§0 \n"
				+ "can charge items.\n"
				+ "The §5§lbottom slot§r§0 \n"
				+ "can accept fuel. It\n"
				+ "also decharges Batrods."
		);
		
		chapter.add(""
				+ "§4§lGenerator:§r§0\n"
				+ "\n"
				+ "To create a Generator place down a furnace and put a §6§lBatrod§r§0\n"
				+ "into the bottom slot."
		);
		
		chapter.add(""
				+ "§4§lSolar Hopper:§r§0\n"
				+ "\n"
				+ "The §4§lSolar Hopper§r§0converts sunlight into a small ammount of power.\n"
				+ "\n"
				+ "Only works at day!"
		);

		
		chapter.add(""
				+ "§4§lSolar Hopper:§r§0\n"
				+ "\n"
				+ "To create place down a hopper.\n"
				+ "Fill it with:\n"
				+ "§fG : Glass\n"
				+ "§1L : Lapis Block\n"
				+ "§6B : Batrod\n"
				+ "§0\n"
				+ "Shape:\n"
				+ "§fG  §1L  §6B  §1L  §fG\n"
		);
		
		return chapter;
	}
}