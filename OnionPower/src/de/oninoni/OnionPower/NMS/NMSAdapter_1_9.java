package de.oninoni.OnionPower.NMS;

import org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_9_R2.IChatBaseComponent;
import net.minecraft.server.v1_9_R2.Packet;
import net.minecraft.server.v1_9_R2.PacketPlayOutChat;

public class NMSAdapter_1_9 extends NMSAdapter{

	@Override
	protected void sendTitleInternal(Player p, String titleText, int fadeIn, int stayOnScreen, int fadeOut) {
		IChatBaseComponent chatTitle = IChatBaseComponent.ChatSerializer.a(titleText);
		Packet<?> title = new PacketPlayOutChat(chatTitle, (byte) 2);
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(title);
	}
	
}
