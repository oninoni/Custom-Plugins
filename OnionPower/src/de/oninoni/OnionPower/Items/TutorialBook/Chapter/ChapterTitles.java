package de.oninoni.OnionPower.Items.TutorialBook.Chapter;

import java.util.ArrayList;
import java.util.List;

public class ChapterTitles extends Chapter{
	@Override
	public List<String> getChapter() {
		List<String> chapter = new ArrayList<>();
		
		chapter.add(""
				+ "§4§lONION POWER§r§0\n"
				+ "\n"
				+ "created by §2§lONINONI\n"
				+ "\n"
				+ "§1Mod is §lWIP§r§0! Bugs\n"
				+ "are to be expected\n"
				+ "\n"
				+ "\n"
				+ "\n"
				+ "§4CLICK HERE§0 to\n"
				+ "update this book"
		);
		
		chapter.add("{\"text\":\""
				+ "§4§lCHAPTERS:§r§0\n"
				+ "\n"
				+ "§1+§2 Basics\n"
				+ "§1+§2 Power\n"
				+ "§1+§2 Machines\n"
				+ "\n"
				+ "§1+§4 Tools\n"
				+ "§1+§4 Upgrades\n"
				+ "§1+§4 Multiblocks\n"
				+ "\"}"
		);
		
		return chapter;
	}
}
