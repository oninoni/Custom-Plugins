package de.oninoni.OnionPower.Items.TutorialBook;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import de.oninoni.OnionPower.OnionPower;
import de.oninoni.OnionPower.Items.TutorialBook.Chapter.ChapterBasics;
import de.oninoni.OnionPower.Items.TutorialBook.Chapter.ChapterPower;
import de.oninoni.OnionPower.Items.TutorialBook.Chapter.ChapterTitles;

public class TutorialBook extends ItemStack{

	protected static OnionPower plugin = OnionPower.get();
	
	public TutorialBook(){
		super(Material.WRITTEN_BOOK);
		BookMeta meta = (BookMeta) getItemMeta();
		
		meta.setTitle("§4Onion Power");
		meta.setAuthor("§6Oninoni");
		
		plugin.getNMSAdapter().addPages(meta, new ChapterTitles().getChapter());
		plugin.getNMSAdapter().addPages(meta, new ChapterBasics().getChapter());
		plugin.getNMSAdapter().addPages(meta, new ChapterPower().getChapter());
		
		setItemMeta(meta);
	}
}