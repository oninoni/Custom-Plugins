package de.oninoni.OnionPower.Map;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapPalette;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import de.oninoni.OnionPower.OnionPower;

public class CustomMapRenderer extends MapRenderer{
	protected static OnionPower plugin = OnionPower.get();

	int count = 0;
	
	@SuppressWarnings("deprecation")
	@Override
	public void render(MapView map, MapCanvas canvas, Player player) {
		for(int x = 0; x < 128; x++){
				canvas.setPixel(x, count, MapPalette.matchColor(255, 0, 0));
		}
		count = (count + 1) % 5120;
	}

}
