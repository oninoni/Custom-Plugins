package de.oninoni.OnionPower.NMS;

import java.lang.reflect.Field;

import org.bukkit.block.Dispenser;
import org.bukkit.block.Dropper;
import org.bukkit.block.Furnace;
import org.bukkit.block.Hopper;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_10_R1.IChatBaseComponent;
import net.minecraft.server.v1_10_R1.Packet;
import net.minecraft.server.v1_10_R1.PacketPlayOutChat;
import net.minecraft.server.v1_10_R1.TileEntityDispenser;
import net.minecraft.server.v1_10_R1.TileEntityDropper;
import net.minecraft.server.v1_10_R1.TileEntityFurnace;
import net.minecraft.server.v1_10_R1.TileEntityHopper;

public class NMSAdapter_1_10 extends NMSAdapter{

	@Override
	protected void setInvNameDispenser(Dispenser dispenser, String name) {
		try {
			Field inventoryField = dispenser.getClass().getDeclaredField("dispenser");
			inventoryField.setAccessible(true);
			TileEntityDispenser tileEntitydispenser = ((TileEntityDispenser) inventoryField.get(dispenser));
			tileEntitydispenser.a(name);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	protected void setInvNameDropper(Dropper dropper, String name) {
		try {
			Field inventoryField = dropper.getClass().getDeclaredField("dropper");
			inventoryField.setAccessible(true);
			TileEntityDropper tileEntitydropper = ((TileEntityDropper) inventoryField.get(dropper));
			tileEntitydropper.a(name);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void setInvNameFurnace(Furnace furnace, String name) {
		try {
			Field inventoryField = furnace.getClass().getDeclaredField("furnace");
			inventoryField.setAccessible(true);
			TileEntityFurnace tileEntityFurnace = ((TileEntityFurnace) inventoryField.get(furnace));
			tileEntityFurnace.a(name);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void setInvNameHopper(Hopper hopper, String name) {
		try {
			Field inventoryField = hopper.getClass().getDeclaredField("hopper");
			inventoryField.setAccessible(true);
			TileEntityHopper tileEntityhopper = ((TileEntityHopper) inventoryField.get(hopper));
			tileEntityhopper.a(name);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void sendTitle(Player p, String titleText, int fadeIn, int stayOnScreen, int fadeOut) {
		IChatBaseComponent chatTitle = IChatBaseComponent.ChatSerializer.a(titleText);
		Packet<?> title = new PacketPlayOutChat(chatTitle, (byte) 2);
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(title);
	}

}
