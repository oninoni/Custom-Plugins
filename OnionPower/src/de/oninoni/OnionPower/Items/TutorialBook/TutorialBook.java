package de.oninoni.OnionPower.Items.TutorialBook;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import de.oninoni.OnionPower.Items.TutorialBook.Chapter.ChapterBasics;
import de.oninoni.OnionPower.Items.TutorialBook.Chapter.ChapterPower;
import de.oninoni.OnionPower.Items.TutorialBook.Chapter.ChapterTitles;

public class TutorialBook extends ItemStack{

	public TutorialBook(){
		super(Material.WRITTEN_BOOK);
		BookMeta meta = (BookMeta) getItemMeta();
		
		meta.setTitle("§4Onion Power");
		meta.setAuthor("§6Oninoni");
		
		ArrayList<String> pages = new ArrayList<>();
		
		pages.add("{\\\"text\\\":\\\"Test\\\"}");
		
		pages.addAll(new ChapterTitles().getChapter());
		pages.addAll(new ChapterBasics().getChapter());
		pages.addAll(new ChapterPower().getChapter());
		
		meta.setPages(pages);
		
		setItemMeta(meta);
	}
}
