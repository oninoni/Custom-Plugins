package de.oninoni.OnionPower.NMS;

import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_10_R1.IChatBaseComponent;
import net.minecraft.server.v1_10_R1.Packet;
import net.minecraft.server.v1_10_R1.PacketPlayOutChat;

public class NMSAdapter_1_10 extends NMSAdapter{

	@Override
	public void sendTitle(Player p, String titleText, int fadeIn, int stayOnScreen, int fadeOut) {
		IChatBaseComponent chatTitle = IChatBaseComponent.ChatSerializer.a(titleText);
		Packet<?> title = new PacketPlayOutChat(chatTitle, (byte) 2);
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(title);
	}

}
