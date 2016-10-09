package de.oninoni.OnionPower.Items.PowerItems;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.oninoni.OnionPower.Items.CustomsItems;

public abstract class PowerItem extends ItemStack{
	
	public int maxPower;
	
	public PowerItem(Material mat, int ammount, short damage, String name) {
		super(mat, ammount, damage);
		
		maxPower = 64000;
		
		ItemMeta itemMeta = this.getItemMeta();
		itemMeta.setDisplayName(name);
		List<String> lore = new ArrayList<>();
		lore.add(0, "§h0");
		lore.add(1, "§60/" + maxPower + " " + CustomsItems.UNIT_NAME);
		lore.add(2, "§h" + UUID.randomUUID());
		itemMeta.setLore(lore);
		itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		this.setItemMeta(itemMeta);
	}

	public int readPower() {
		return Integer.parseInt(this.getItemMeta().getLore().get(0).substring(2));
	}

	public void setPower(int power) {
		power = Math.min(power, maxPower);
		ItemMeta itemMeta = this.getItemMeta();
		List<String> lore = itemMeta.getLore();
		lore.set(0, "§h" + power);
		lore.set(1, "§6" + power + "/" + maxPower + " " + CustomsItems.UNIT_NAME);
		itemMeta.setLore(lore);
		this.setItemMeta(itemMeta);
	}
	
}
