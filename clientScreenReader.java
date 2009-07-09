import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class clientScreenReader extends Canvas {
	public BufferedImage image;

	@Override
	public void paint(Graphics g) {
		g.drawImage(image, 0, 0, null);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(image.getWidth(),image.getHeight());
	}
}
