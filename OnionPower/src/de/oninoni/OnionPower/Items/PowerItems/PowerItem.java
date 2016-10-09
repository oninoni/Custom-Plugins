package de.oninoni.OnionPower.Items.PowerItems;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.oninoni.OnionPower.OnionPower;
import de.oninoni.OnionPower.Items.CustomsItems;

public class PowerItem extends ItemStack{
	protected static OnionPower plugin = OnionPower.get();

	private String itemName;
	public int maxPower;
	
	public PowerItem(Material mat, int ammount, short damage, String name, int power) {
		super(mat, ammount, damage);
		
		itemName = name;
		maxPower = 64000;
		
		create(power);
	}
	
	public PowerItem(ItemStack item, String name){
		super(item == null ? new ItemStack(Material.AIR) : item);
		
		itemName = name;
		maxPower = 64000;
	}
	
	public PowerItem(ItemStack item){
		this(item, "");
	}
	
	private void create(int power){
		ItemMeta itemMeta = getItemMeta();
		itemMeta.setDisplayName(itemName);
		List<String> lore = new ArrayList<>();
		lore.add(0, "§h" + power);
		lore.add(1, "§6" + power + "/" + maxPower + " " + CustomsItems.UNIT_NAME);
		lore.add(2, "§h" + UUID.randomUUID());
		itemMeta.setLore(lore);
		itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		setItemMeta(itemMeta);
	}
	
	public boolean check(){
		ItemMeta itemMeta = getItemMeta();
		if(itemMeta == null || getType() == Material.AIR)return false;
		if(itemMeta.getDisplayName() == null)return false;
		List<String> lore = itemMeta.getLore();
		if(lore == null || lore.size() <= 2)return false;
		if(!(lore.get(0).startsWith("§h") && lore.get(1).startsWith("§6") && lore.get(2).startsWith("§h")))return false;
		if(itemName != "" && !itemMeta.getDisplayName().equals(itemName))return false;
		return true;
	}

	public int readPower() {
		return Integer.parseInt(getItemMeta().getLore().get(0).substring(2));
	}

	public void setPower(int power) {
		power = Math.min(power, maxPower);
		ItemMeta itemMeta = getItemMeta();
		List<String> lore = itemMeta.getLore();
		lore.set(0, "§h" + power);
		lore.set(1, "§6" + power + "/" + maxPower + " " + CustomsItems.UNIT_NAME);
		itemMeta.setLore(lore);
		this.setItemMeta(itemMeta);
	}
	
}
