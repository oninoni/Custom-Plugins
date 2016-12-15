package de.oninoni.OnionPower.Items.TutorialBook.Chapter;

import java.util.ArrayList;
import java.util.List;

import de.oninoni.OnionPower.Items.TutorialBook.BookRecipes;
import de.oninoni.OnionPower.Machines.MachineTemplates;
import de.oninoni.OnionPower.Machines.DispenserBased.Sorter;

public class ChapterMachines extends Chapter{

	@Override
	public List<String> getChapter() {
		List<String> chapter = new ArrayList<>();
		
		chapter.add("{\"text\":\""
				+ "§4§lMachines:§r§0\n"
				+ "\n"
				+ "This chapter handles the basic §4§lMachines§r§0 wich are only one block in size.\n"
				+ "\n"
				+ "Bigger machines are in the Multiblock chapter!"
				+ "\"}"
		);
		
		chapter.add("{\"text\":\""
				+ "§4§lElectrical Furnace:§r§0\n"
				+ "\n"
				+ "The §4§lElectrical\n§4§lFurnace§r§0 works like a normal furnace, but instead of fuel it uses electrical power to heat its contents."
				+ "\"}"
		);
		
		chapter.add("{\"text\":\""
				+ "§4§lElectrical Furnace:§r§0\n"
				+ "\n"
				+ "To create an\n§4§lElectrical Furnace§r§0\nplace down a furnace and put a §6§lBatrod§r§0\n"
				+ "into the §5§ltop slot§r§0."
				+ "\"}"
		);
		
		chapter.add("{\"text\":\"\",\"extra\":["
				+ BookRecipes.createRecipe(MachineTemplates.buildTemplates.get(Sorter.class.getName()))
				+ "]}"
		);
		
		return chapter;
	}

}
