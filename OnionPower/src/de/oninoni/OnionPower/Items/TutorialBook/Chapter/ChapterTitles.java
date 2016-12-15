package de.oninoni.OnionPower.Items.TutorialBook.Chapter;

import java.util.ArrayList;
import java.util.List;

public class ChapterTitles extends Chapter{
	@Override
	public List<String> getChapter() {
		List<String> chapter = new ArrayList<>();
		
		chapter.add("{\"text\":\""
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
				+ "\"}"
		);
		
		chapter.add("[{\"text\":\""
				+ "§4§lCHAPTERS:§r§0\n"
				+ "\n\"},"
				+ "{\"text\":\"§1+§2 Basics\n\",\"clickEvent\":{\"action\":\"change_page\",\"value\":\"3\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Go to Chapter\"}},"
				+ "{\"text\":\"§1+§2 Power\n\",\"clickEvent\":{\"action\":\"change_page\",\"value\":\"6\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Go to Chapter\"}},"
				+ "{\"text\":\"§1+§2 Machines\n\",\"clickEvent\":{\"action\":\"change_page\",\"value\":\"12\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Go to Chapter\"}},"
				+ "{\"text\":\"\n\"},"
				+ "{\"text\":\"§1+§4 Tools\n\",\"clickEvent\":{\"action\":\"change_page\",\"value\":\"1\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Go to Chapter\"}},"
				+ "{\"text\":\"§1+§4 Upgrades\n\",\"clickEvent\":{\"action\":\"change_page\",\"value\":\"1\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Go to Chapter\"}},"
				+ "{\"text\":\"§1+§4 Multiblocks\n\",\"clickEvent\":{\"action\":\"change_page\",\"value\":\"1\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Go to Chapter\"}}"
				+ "]"
		);
		
		return chapter;
	}
}
