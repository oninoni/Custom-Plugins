package de.oninoni.OnionPower.Items;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CustomsItems {
	public static final String UNIT_NAME = "OnionPower";

	public static ItemStack getGlassPane(byte metaData, String title) {
		ItemStack pane = new ItemStack(Material.STAINED_GLASS_PANE, 1, metaData);
		ItemMeta meta = pane.getItemMeta();
		meta.setDisplayName(title);
		pane.setItemMeta(meta);
		return pane;
	}

	public static ItemStack getGlassPane(byte metaData, String title, List<String> lore) {
		ItemStack pane = getGlassPane(metaData, title);
		ItemMeta meta = pane.getItemMeta();
		meta.setLore(lore);
		pane.setItemMeta(meta);
		return pane;
	}

	public static ItemStack getMinerPickAxe() {
		return getMinerPickAxe((short) 0);
	}

	public static ItemStack getMinerPickAxe(short durability) {
		ItemStack pick = new ItemStack(Material.IRON_PICKAXE);
		ItemMeta meta = pick.getItemMeta();
		meta.setDisplayName("§4Internal Pickaxe");
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		pick.setItemMeta(meta);
		pick.setDurability(durability);
		return pick;
	}

	public static ItemStack getLaserPrism() {
		ItemStack prism = new ItemStack(Material.STAINED_GLASS, 1, (short) 10);
		ItemMeta meta = prism.getItemMeta();
		meta.setDisplayName("§5Laser Prism");
		prism.setItemMeta(meta);
		return prism;
	}

	public static ItemStack getCraftingCore() {
		ItemStack craftingCore = new ItemStack(Material.WORKBENCH);
		ItemMeta meta = craftingCore.getItemMeta();
		meta.setDisplayName("§3Crafting Core");
		craftingCore.setItemMeta(meta);
		return craftingCore;
	}
}