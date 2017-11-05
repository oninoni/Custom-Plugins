package de.oninoni.OnionPower.NMS;

import java.lang.reflect.Field;
import java.util.List;

import org.bukkit.block.Dispenser;
import org.bukkit.block.Dropper;
import org.bukkit.block.Furnace;
import org.bukkit.block.Hopper;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftDispenser;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftDropper;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftFurnace;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftHopper;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftMetaBook;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.BookMeta;

import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_12_R1.PacketPlayOutTitle;
import net.minecraft.server.v1_12_R1.PacketPlayOutTitle.EnumTitleAction;

public class NMSAdapter_1_12 extends NMSAdapter{

	@Override
	protected void setInvNameDispenser(Dispenser dispenser, String name) {
		try {
			((CraftDispenser) dispenser).setCustomName(name);
			((CraftDispenser) dispenser).update();
			
			/*
			Field inventoryField = dispenser.getClass().getDeclaredField("dispenser");
			inventoryField.setAccessible(true);
			TileEntityDispenser tileEntitydispenser = ((TileEntityDispenser) inventoryField.get(dispenser));
			tileEntitydispenser.setCustomName(name);
			*/
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	protected void setInvNameDropper(Dropper dropper, String name) {
		try {
			((CraftDropper) dropper).setCustomName(name);
			((CraftDropper) dropper).update();
			
			/*
			Field inventoryField = dropper.getClass().getDeclaredField("dropper");
			inventoryField.setAccessible(true);
			TileEntityDropper tileEntitydropper = ((TileEntityDropper) inventoryField.get(dropper));
			tileEntitydropper.setCustomName(name);
			*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void setInvNameFurnace(Furnace furnace, String name) {
		try {
			((CraftFurnace) furnace).setCustomName(name);
			((CraftFurnace) furnace).update();
			
			/*
			Field inventoryField = furnace.getClass().getDeclaredField("furnace");
			inventoryField.setAccessible(true);
			TileEntityFurnace tileEntityFurnace = ((TileEntityFurnace) inventoryField.get(furnace));
			tileEntityFurnace.setCustomName(name);
			*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void setInvNameHopper(Hopper hopper, String name) {
		try {
			((CraftHopper) hopper).setCustomName(name);
			((CraftHopper) hopper).update();
			
			/*
			Field inventoryField = hopper.getClass().getDeclaredField("hopper");
			inventoryField.setAccessible(true);
			TileEntityHopper tileEntityhopper = ((TileEntityHopper) inventoryField.get(hopper));
			tileEntityhopper.setCustomName(name);
			*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void sendTitle(Player p, String titleText, int fadeIn, int stayOnScreen, int fadeOut) {
		IChatBaseComponent chatTitle = IChatBaseComponent.ChatSerializer.a(titleText);
		PacketPlayOutTitle title = new PacketPlayOutTitle(EnumTitleAction.ACTIONBAR, chatTitle);
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(title);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void addPage(BookMeta meta, String json) {
		try {
			Field pagesField = CraftMetaBook.class.getDeclaredField("pages");
			pagesField.setAccessible(true);
			List<IChatBaseComponent> pages = (List<IChatBaseComponent>) pagesField.get(meta);
			IChatBaseComponent page = ChatSerializer.a(json);
			pages.add(page);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}