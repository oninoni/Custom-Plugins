package de.oninoni.OnionPower.Items.TutorialBook.Chapter;

import java.util.ArrayList;
import java.util.List;

public class ChapterBasics extends Chapter{
	@Override
	public List<String> getChapter() {
		List<String> chapter = new ArrayList<>();
		
		chapter.add(""
				+ "§4§lBasics:§r§0\n"
				+ "\n§r"
				+ "This mod add a unit of §4§lelectrical power§r§0 to minecraft.\n"
				+ "You will be able to use this power to your advantage and create §4§lMachines§r§0 and §4§lTools§r§0 to aid you in your adventure.\n"
		);
		
		chapter.add(""
				+ "§4§lBatrod:§r§0\n"
				+ "\n"
				+ "The §4§lBatrod§r§0\nstores energy and is the base ingredient for all machines!\n"
		);
		
		chapter.add(""
				+ "§4§lBatrod:§r§0\n"
				+ "\n"
				+ "Recipe:\n"
				+ "§6B : Blaze Rod\n"
				+ "§eG : Gold Bar\n"
				+ "§4R : Redstone\n"
				+ "§0\n"
				+ "Shape:\n"
				+ "§4R  §4R  §4R\n"
				+ "§eG  §6B  §eG\n"
				+ "§4R  §4R  §4R"
		);
		
		return chapter;
	}
}
