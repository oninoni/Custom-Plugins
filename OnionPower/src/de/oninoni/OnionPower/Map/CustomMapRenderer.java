package de.oninoni.OnionPower.Map;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import de.oninoni.OnionPower.OnionPower;

public class CustomMapRenderer extends MapRenderer{
	protected static OnionPower plugin = OnionPower.get();

	int count = 0;
	
	@Override
	public void render(MapView map, MapCanvas canvas, Player player) {
		for(int x = 0; x < 128; x++){
			for(int y = 0; y < 128; y++){
				canvas.setPixel(x, y, (byte) (count / 20));
			}
		}
		count = (count + 1) % 5120;
		
		if(count % 20.0 == 0)
			plugin.getLogger().info(""+count / 20);
	}

}
