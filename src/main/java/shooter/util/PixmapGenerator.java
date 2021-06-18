package shooter.util;

import com.badlogic.gdx.graphics.Pixmap;

public class PixmapGenerator {
	public static Pixmap getTexture(int w, int h, float r, float g, float b, float a) {
		Pixmap map = new Pixmap(w, h, Pixmap.Format.RGB888);
		map.setColor(r, g, b, a);
		for(int i=0; i<w; i++) {
			for(int j=0; j<h; j++) {
				map.drawPixel(i, j);
			}
		}
		return map;
	}
}
