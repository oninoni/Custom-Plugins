package de.oninoni.OnionPower.Items.PowerItems;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.oninoni.OnionPower.OnionPower;
import de.oninoni.OnionPower.Items.PowerItems.PowerTools.ElectricalAxe;
import de.oninoni.OnionPower.Items.PowerItems.PowerTools.ElectricalBoots;
import de.oninoni.OnionPower.Items.PowerItems.PowerTools.ElectricalChestplate;
import de.oninoni.OnionPower.Items.PowerItems.PowerTools.ElectricalElytra;
import de.oninoni.OnionPower.Items.PowerItems.PowerTools.ElectricalHelmet;
import de.oninoni.OnionPower.Items.PowerItems.PowerTools.ElectricalHoe;
import de.oninoni.OnionPower.Items.PowerItems.PowerTools.ElectricalLeggings;
import de.oninoni.OnionPower.Items.PowerItems.PowerTools.ElectricalPickaxe;
import de.oninoni.OnionPower.Items.PowerItems.PowerTools.ElectricalShovel;
import de.oninoni.OnionPower.Items.PowerItems.PowerTools.ElectricalSword;
import de.oninoni.OnionPower.Items.Statics.CustomsItems;
import de.oninoni.OnionPower.Machines.Machine;

public class PowerItem extends ItemStack{
	protected static OnionPower plugin = OnionPower.get();

	public static final ArrayList<Class<? extends PowerItem>> classes;
	
	static {
		classes = new ArrayList<>();
		classes.add(ElectricalAxe.class);
		classes.add(ElectricalPickaxe.class);
		classes.add(ElectricalHoe.class);
		classes.add(ElectricalShovel.class);
		classes.add(ElectricalSword.class);
		classes.add(ElectricalHelmet.class);
		classes.add(ElectricalChestplate.class);
		classes.add(ElectricalLeggings.class);
		classes.add(ElectricalBoots.class);
		classes.add(Batrod.class);
		classes.add(ElectricalElytra.class);
	}
	
	public Material getVisibleType(){
		return getType();
	}
	
	private String itemName;
	public int maxPower;
	
	Machine machine;
	
	private void init(String name, int power)
	{
		itemName = name;
		maxPower = 64000;
		
		create(power);
	}
	
	public PowerItem(int amount, short damage, String name, int power) {
		super(Material.SPONGE, amount, damage);
		setType(getOriginalType());
		init(name, power);
	}
	
	public PowerItem(int amount, short damage, String name, Machine m) {
		super(Material.SPONGE, amount, damage);
		setType(getOriginalType());
		machine = m;
		init(name, m.getPower());
	}

	public PowerItem(ItemStack item, String name){
		super(item == null ? new ItemStack(Material.AIR) : item);
		
		itemName = name;
		maxPower = 64000;
	}
	
	public PowerItem(ItemStack item){
		this(item, "");
		// TODO make check better with static function
	}
	
	protected Material getOriginalType(){
		return Material.SPONGE;
	}
	
	public void onCraft(PrepareItemCraftEvent e){
		ItemStack result = e.getInventory().getResult();
		
		for (Class<? extends PowerItem> c : classes)
		{
			try {
				PowerItem item = (PowerItem) c.getConstructor(ItemStack.class).newInstance(result);
				if (item.check()){
					//plugin.getServer().broadcastMessage("" + c.getName());
					item.onCraft(e);
					break;
				}
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	protected void create(int power){
		ItemMeta itemMeta = getItemMeta();
		itemMeta.setDisplayName(itemName);
		List<String> lore = new ArrayList<>();
		lore.add(0, "�h" + power);
		lore.add(1, "�6" + power + "/" + maxPower + " " + CustomsItems.UNIT_NAME);
		lore.add(2, "�h" + UUID.randomUUID());
		itemMeta.setLore(lore);
		itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		setItemMeta(itemMeta);
	}
	
	public boolean check(){
		if(!checkName())
			return false;
		ItemMeta itemMeta = getItemMeta();
		List<String> lore = itemMeta.getLore();
		if(lore == null || lore.size() <= 2)
			return false;
		if(!(lore.get(0).startsWith("�h") && lore.get(1).startsWith("�6") && lore.get(2).startsWith("�h")))
			return false;
		return true;
	}
	
	public boolean checkName(){
		ItemMeta itemMeta = getItemMeta();
		if(itemMeta == null || getType() == Material.AIR)
			return false;
		if(itemMeta.getDisplayName() == null)
			return false;
		if(itemName != "" && !itemMeta.getDisplayName().equals(itemName))
			return false;
		return true;
	}
	
	public void fixItemFromCreativeSet(ItemStack item){
		//plugin.getLogger().info(item.getType() + "/" + getOriginalType());
		item.setType(getOriginalType());
		
		ItemMeta itemMeta = getItemMeta();
		List<String> lore = itemMeta.getLore();
		create(Integer.parseInt(lore.get(0).split("/")[0].substring(2)));
		
		itemMeta = item.getItemMeta();
		itemMeta.setLore(getItemMeta().getLore());
		item.setItemMeta(itemMeta);
	}

	public int getPower() {
		return Integer.parseInt(getItemMeta().getLore().get(0).substring(2));
	}

	public void setPower(int power) {
		power = Math.min(power, maxPower);
		ItemMeta itemMeta = getItemMeta();
		List<String> lore = itemMeta.getLore();
		lore.set(0, "�h" + power);
		lore.set(1, "�6" + power + "/" + maxPower + " " + CustomsItems.UNIT_NAME);
		itemMeta.setLore(lore);
		this.setItemMeta(itemMeta);
	}
	
}
