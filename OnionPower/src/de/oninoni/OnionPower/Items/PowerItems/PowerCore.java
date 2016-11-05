package de.oninoni.OnionPower.Items.PowerItems;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.oninoni.OnionPower.Items.Statics.CustomsItems;
import de.oninoni.OnionPower.Machines.Machine;

public class PowerCore extends PowerItem{
	private static final String NAME = "§4Power Core";
	
	public PowerCore(Machine m){
		super(1, (short) 0, NAME, m);
	}
	
	public PowerCore(ItemStack item, Machine m){
		super(item, NAME);
		machine = m;
	}
	
	@Override
	protected Material getOriginalType() {
		return Material.END_CRYSTAL;
	}
	
	@Override
	protected void create(int power) {
		super.create(power);
		//plugin.getLogger().info("" + machine);
		ItemMeta itemMeta = getItemMeta();
		List<String> lore = itemMeta.getLore();
		if (machine.getMaxPowerOutput() == 0) {
			lore.add(3, "§6Using: " + machine.getPowerOutputTotal() + " " + CustomsItems.UNIT_NAME);
		} else {
			lore.add(3, "§6Output: " + machine.getPowerOutputTotal() + " / " + machine.getMaxPowerOutput() + " "
					+ CustomsItems.UNIT_NAME);
		}
		if (machine.getMaxPowerInput() == 0) {
			lore.add(4, "§6Generating: " + machine.getPowerIntputTotal() + " " + CustomsItems.UNIT_NAME);
		} else {
			lore.add(4, "§6Input: " + machine.getPowerIntputTotal() + " / " + machine.getMaxPowerInput() + " "
					+ CustomsItems.UNIT_NAME);
		}
		itemMeta.setLore(lore);
		setItemMeta(itemMeta);
	}
	
	@Override
	public void setPower(int power) {
		super.setPower(power);
		ItemMeta itemMeta = getItemMeta();
		List<String> lore = itemMeta.getLore();
		if (machine.getMaxPowerOutput() == 0) {
			lore.set(3, "§6Using: " + machine.getPowerOutputTotal() + " " + CustomsItems.UNIT_NAME);
		} else {
			lore.set(3, "§6Output: " + machine.getPowerOutputTotal() + " / " + machine.getMaxPowerOutput() + " "
					+ CustomsItems.UNIT_NAME);
		}
		if (machine.getMaxPowerInput() == 0) {
			lore.set(4, "§6Generating: " + machine.getPowerIntputTotal() + " " + CustomsItems.UNIT_NAME);
		} else {
			lore.set(4, "§6Input: " + machine.getPowerIntputTotal() + " / " + machine.getMaxPowerInput() + " "
					+ CustomsItems.UNIT_NAME);
		}
		itemMeta.setLore(lore);
		setItemMeta(itemMeta);
	}
}
