package edu.js.interpreter.plugins;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.util.Random;
import java.util.Stack;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import edu.js.interpreter.gui.ide;
import edu.js.interpreter.preprocessed.interpreting_objects.pointer;
import edu.js.interpreter.processing.pascal_plugin;

public class scar_robot implements pascal_plugin {
	ide ide;

	Robot r;

	Random rand;

	WritableRaster lastCapture;

	Point lastCaptureOffset;

	long lastScreenCapTime;

	int maxDelay = 5; // TODO find a good value for this.

	public void update_screen(Rectangle rect) {
		long currenttime = System.currentTimeMillis();
		Rectangle bounds = (Rectangle) lastCapture.getBounds().clone();
		bounds.translate(lastCaptureOffset.x, lastCaptureOffset.y);
		if (currenttime - lastScreenCapTime > maxDelay
				|| !bounds.contains(rect)) {
			lastCapture = r.createScreenCapture(rect).getRaster();
			lastScreenCapTime = currenttime;
			lastCaptureOffset.x = rect.x;
			lastCaptureOffset.y = rect.y;
		}
	}

	public scar_robot(ide i) {
		this.ide = i;
		try {
			this.r = new Robot();
			this.rand = new Random();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	public void GetMousePos(pointer<Integer> x, pointer<Integer> y) {
		Point windowloc = ide.connection.getWindowLocation(ide.window);
		Point mouseloc = MouseInfo.getPointerInfo().getLocation();
		x.set(mouseloc.x - windowloc.x);
		y.set(mouseloc.y - windowloc.y);
	}

	public void MoveMouse(int x, int y) {
		Point windowloc = ide.connection.getWindowLocation(ide.window);
		r.mouseMove(x + windowloc.x, y + windowloc.y);
	}

	public void ClickMouse(int x, int y, boolean left) {
		Point windowloc = ide.connection.getWindowLocation(ide.window);
		ClickMouse(x + windowloc.x, y + windowloc.y,
				left ? InputEvent.BUTTON1_DOWN_MASK
						: InputEvent.BUTTON2_DOWN_MASK);
	}

	public void ClickMouseMid(int x, int y) {
		Point windowloc = ide.connection.getWindowLocation(ide.window);
		ClickMouse(x + windowloc.x, y + windowloc.y,
				InputEvent.BUTTON3_DOWN_MASK);
	}

	public void ClickMouse(int x, int y, int mask) {
		Point windowloc = ide.connection.getWindowLocation(ide.window);
		long mouseclickduration = Math.max(
				(long) (rand.nextGaussian() * 20) + 100, 1);
		if (!MouseInfo.getPointerInfo().getLocation().equals(
				new Point(x + windowloc.x, y + windowloc.y))) {
			r.mouseMove(x, y);
		}

		r.mousePress(mask);
		try {
			Thread.sleep(mouseclickduration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		r.mouseRelease(mask);
	}

	public void HoldMouse(int x, int y, boolean left) {
		Point windowloc = ide.connection.getWindowLocation(ide.window);
		HoldMouse(x + windowloc.x, y + windowloc.y,
				left ? InputEvent.BUTTON1_DOWN_MASK
						: InputEvent.BUTTON2_DOWN_MASK);
	}

	public void HoldMouseMid(int x, int y) {
		Point windowloc = ide.connection.getWindowLocation(ide.window);
		HoldMouse(x + windowloc.x, y + windowloc.y,
				InputEvent.BUTTON3_DOWN_MASK);
	}

	public void HoldMouse(int x, int y, int buttonmask) {
		Point windowloc = ide.connection.getWindowLocation(ide.window);
		if (!MouseInfo.getPointerInfo().getLocation().equals(
				new Point(x + windowloc.x, y + windowloc.y))) {
			r.mouseMove(x, y);
		}
		r.mousePress(buttonmask);
	}

	public void ReleaseMouse(int x, int y, boolean left) {
		Point windowloc = ide.connection.getWindowLocation(ide.window);
		ReleaseMouse(x + windowloc.x, y + windowloc.y,
				left ? InputEvent.BUTTON1_DOWN_MASK
						: InputEvent.BUTTON2_DOWN_MASK);
	}

	public void ReleaseMouseMid(int x, int y) {
		Point windowloc = ide.connection.getWindowLocation(ide.window);
		ReleaseMouse(x + windowloc.x, y + windowloc.y,
				InputEvent.BUTTON3_DOWN_MASK);
	}

	public void ReleaseMouse(int x, int y, int buttonmask) {
		Point windowloc = ide.connection.getWindowLocation(ide.window);
		if (!MouseInfo.getPointerInfo().getLocation().equals(
				new Point(x + windowloc.x, y + windowloc.y))) {
			r.mouseMove(x, y);
		}
		r.mouseRelease(buttonmask);
	}

	public void SendKeys(String tosend) {
		for (char c : tosend.toCharArray()) {
			GenerateKeyTypedWait(c, 0, 0);
		}
	}

	public void SendKeysWait(String text, int wait, int randomness) {
		for (char c : text.toCharArray()) {
			GenerateKeyTypedWait(c, wait, randomness);
		}
	}

	/*
	 * I got these values by having scar spit them out, and then I consolodated
	 * some.
	 */
	public static int GetKeyCode(char c) {
		char[] badjava = new char[] { '!', '@', '#', '$', '%', '^', '&', '*',
				'(', ')', '_', '+', '<', '>', '?', '|', '{', '}', ':', '"', '~' };
		char[] goodjava = new char[] { '1', '2', '3', '4', '5', '6', '7', '8',
				'9', '0', '-', '=', ',', '.', '/', '\\', '[', ']', ';', '\'',
				'`' };
		for (int i = 0; i < badjava.length; i++) {
			if (c == badjava[i]) {
				c = goodjava[i];
			}
		}
		switch (c) {
		case 0:
			return 50;
		case 1:
			return 65;
		case 2:
			return 66;
		case 3:
			return 3;
		case 4:
		case 5:
		case 6:
		case 7:
			return c + 64;
		case 8:
			return 8;
		case 9:
			return 9;
		case '\n':
			return KeyEvent.VK_ENTER;
		case 11:
			return 75;
		case 12:
			return 76;
		case 13:
			return 13;
		case 14:
		case 15:
		case 16:
		case 17:
		case 18:
		case 19:
		case 20:
		case 21:
		case 22:
		case 23:
		case 24:
		case 25:
		case 26:
			return c + 64;
		case 27:
			return 27;
		case 28:
			return 220;
		case 29:
			return 221;
		case 30:
			return 54;
		case 31:
			return 189;
		case 32:
			return 32;
		case 33:
			return 49;
		case 34:
			return 222;
		case 35:
		case 36:
		case 37:
			return c + 16;
		case 38:
			return 55;
		case 39:
			return 222;
		case 40:
			return 57;
		case 41:
			return 48;
		case '*':
			return KeyEvent.VK_ASTERISK;
		case '+':
			return KeyEvent.VK_PLUS;
		case ',':
			return KeyEvent.VK_COMMA;
		case '-':
			return KeyEvent.VK_MINUS;
		case '.':
			return KeyEvent.VK_PERIOD;
		case '/':
			return KeyEvent.VK_SLASH;
		case 48:
		case 49:
		case 50:
		case 51:
		case 52:
		case 53:
		case 54:
		case 55:
		case 56:
		case 57:
			return c;
		case 58:
		case ';':
			return KeyEvent.VK_SEMICOLON;
		case 60:
			return 188;
		case '=':
			return KeyEvent.VK_EQUALS;
		case 62:
			return 190;
		case 63:
			return 191;
		case 64:
			return 50;
		case 65:
		case 66:
		case 67:
		case 68:
		case 69:
		case 70:
		case 71:
		case 72:
		case 73:
		case 74:
		case 75:
		case 76:
		case 77:
		case 78:
		case 79:
		case 80:
		case 81:
		case 82:
		case 83:
		case 84:
		case 85:
		case 86:
		case 87:
		case 88:
		case 89:
		case 90:
			return c;
		case '[':
			return KeyEvent.VK_OPEN_BRACKET;
		case '\\':
			return KeyEvent.VK_BACK_SLASH;
		case ']':
			return KeyEvent.VK_CLOSE_BRACKET;
		case 94:
			return 54;
		case 95:
			return 189;
		case 96:
			return 192;
		case 97:
		case 98:
		case 99:
		case 100:
		case 101:
		case 102:
		case 103:
		case 104:
		case 105:
		case 106:
		case 107:
		case 108:
		case 109:
		case 110:
		case 111:
		case 112:
		case 113:
		case 114:
		case 115:
		case 116:
		case 117:
		case 118:
		case 119:
		case 120:
		case 121:
		case 122:
			return c - 32;
		case 123:
		case 124:
		case 125:
			return c + 96;
		case 126:
			return 192;
		case 127:
			return 8;
		default:
			return 255;
		}
	}

	/*
	 * This only works with my keyboard layout. I assume it is pretty common.
	 */
	boolean needsShift(char c) {
		char[] badjava = new char[] { '!', '@', '#', '$', '%', '^', '&', '*',
				'(', ')', '_', '+', '<', '>', '?', '|', '{', '}', ':', '"', '~' };
		if (Character.isUpperCase(c)) {
			return true;
		}
		for (char ch : badjava) {
			if (ch == c) {
				return true;
			}
		}
		return false;
	}

	public void GenerateKeyTypedWait(char c, int wait, int random) {
		if (needsShift(c)) {
			r.keyPress(KeyEvent.VK_SHIFT);
			WaitRandom(wait, random);
		}

		r.keyPress(GetKeyCode(c));
		WaitRandom(wait, random);
		r.keyRelease(GetKeyCode(c));
		if (needsShift(c)) {
			WaitRandom(wait, random);
			r.keyRelease(KeyEvent.VK_SHIFT);
		}
		WaitRandom(wait, random);
	}

	public void WaitRandom(int wait, int random) {
		scar_script_control.wait(wait + random == 0 ? 0 : rand.nextInt(random));
	}

	public void KeyDown(int key) {
		r.keyPress(key);
	}

	public void KeyUp(int key) {
		r.keyRelease(key);
	}

	public void SendArrow(int key) {
		SendArrowWait(key, 0);
	}

	public void SendArrowWait(int key, int waittime) {
		int keycode = 0;
		switch (key) {
		case 0:
			keycode = KeyEvent.VK_UP;
			break;
		case 1:
			keycode = KeyEvent.VK_RIGHT;
			break;
		case 2:
			keycode = KeyEvent.VK_DOWN;
			break;
		case 3:
			keycode = KeyEvent.VK_LEFT;
		}
		if (keycode != 0) {
			r.keyPress(keycode);
			scar_script_control.wait(waittime);
			r.keyRelease(keycode);
		} else {
			System.err.println("Expected 0-3 for sendkeys");
		}
	}

	public long GetClientWindowHandle() {
		return ide.window;
	}

	public void SetClientWindowHandle(long window) {
		ide.window = window;
	}

	public void FindWindow(String name) {
		ide.window = ide.connection.getWindowByName(name);
	}

	public void ActivateClient() {
		ide.connection.ActivateWindow(ide.window);
	}

	public void GetClientDimensions(pointer<Integer> x, pointer<Integer> y) {
		Point result = ide.connection.GetWindowDimensions(ide.window);
		x.set(result.x);
		y.set(result.y);
	}

	public int GetColor(int x, int y) {
		Point windowloc = ide.connection.getWindowLocation(ide.window);
		x += windowloc.x;
		y += windowloc.y;
		return r.getPixelColor(x, y).getRGB();
	}

	public boolean FindColor(pointer<Integer> x, pointer<Integer> y, int color,
			int xstart, int ystart, int xend, int yend) {
		Color jcolor = new Color(color);
		Point windowloc = ide.connection.getWindowLocation(ide.window);
		xstart += windowloc.x;
		ystart += windowloc.y;
		xend += windowloc.x;
		yend += windowloc.y;
		update_screen(new Rectangle(xstart, ystart, xend - xstart, yend
				- ystart));
		xstart -= lastCaptureOffset.x;
		xend -= lastCaptureOffset.x;
		ystart -= lastCaptureOffset.y;
		yend -= lastCaptureOffset.y;
		for (int i = xstart; i <= xend; i++) {
			for (int j = ystart; j < yend; j++) {
				if (ColorsSame(jcolor, lastCapture, i, j)) {
					x.set(i + lastCaptureOffset.x);
					y.set(j + lastCaptureOffset.y);
					return true;
				}
			}
		}
		return false;
	}

	public boolean FindColorTolerance(pointer<Integer> x, pointer<Integer> y,
			int color, int xstart, int ystart, int xend, int yend, int tolerance) {
		Point windowloc = ide.connection.getWindowLocation(ide.window);
		Color jcolor = new Color(color);
		xstart += windowloc.x;
		ystart += windowloc.y;
		xend += windowloc.x;
		yend += windowloc.y;
		update_screen(new Rectangle(xstart, ystart, xend - xstart, yend
				- ystart));
		xstart -= lastCaptureOffset.x;
		xend -= lastCaptureOffset.x;
		ystart -= lastCaptureOffset.y;
		yend -= lastCaptureOffset.y;
		for (int i = xstart; i <= xend; i++) {
			for (int j = ystart; j < yend; j++) {
				if (SimilarColor(jcolor, tolerance, lastCapture, i, j)) {
					x.set(i + lastCaptureOffset.x);
					y.set(j + lastCaptureOffset.y);
					return true;
				}
			}
		}
		return false;
	}

	public boolean FindColorSpiral2(pointer<Integer> x, pointer<Integer> y,
			int color, int xs, int ys, int xe, int ye) {
		boolean found = FindColorSpiral(x, y, color, xs, ys, xe, ye);
		if (!found) {
			return false;
		} else {
			Point center = findClusterCenter(x.get(), y.get());
			x.set(center.x);
			y.set(center.y);
			return true;
		}
	}

	/*
	 * Hey, this might be grossly inefficient!
	 */
	Point findClusterCenter(int x, int y) {
		x -= lastCaptureOffset.x;
		y -= lastCaptureOffset.y;
		boolean[][] hasVisited = new boolean[lastCapture.getWidth()][lastCapture
				.getHeight()];
		int numincluster = 1;
		Stack<Point> need_to_visit = new Stack<Point>();
		need_to_visit.push(new Point(x, y));
		while (!need_to_visit.isEmpty()) {
			Point next = need_to_visit.pop();
			hasVisited[next.x][next.y] = true;
			x += next.x;
			y += next.y;
			numincluster++;
			if (next.x != lastCapture.getHeight() - 1
					&& !hasVisited[next.x][next.y + 1]) {
				need_to_visit.push(new Point(next.x, next.y + 1));
			}
			if (next.x != 0 && !hasVisited[next.x][next.y - 1]) {
				need_to_visit.push(new Point(next.x, next.y - 1));
			}
			if (next.x != lastCapture.getWidth() - 1
					&& !hasVisited[next.x + 1][next.y]) {
				need_to_visit.push(new Point(next.x + 1, next.y));
			}
			if (next.x != 0 && !hasVisited[next.x - 1][next.y]) {
				need_to_visit.push(new Point(next.x - 1, next.y));
			}
		}
		return new Point(((int) x / numincluster) + lastCaptureOffset.x,
				((int) y / numincluster) + lastCaptureOffset.y);
	}

	/*
	 * This method will actually double check certain pixels: it will test about
	 * 1.12 times as many pixels as it needs to. However, I think it is still
	 * fast. I could do better with clipping though.
	 */
	public boolean FindColorSpiral(pointer<Integer> x, pointer<Integer> y,
			int color, int xs, int ys, int xe, int ye) {
		return FindColorSpiralTolerance(x, y, color, xs, ys, xe, ye, 0);
	}

	public boolean FindColorCircle(pointer<Integer> x, pointer<Integer> y,
			int color, int mx, int my, int radius) {
		return FindColorCirlceTolerance(x, y, color, mx, my, radius, 0);
	}

	public boolean FindColorCirlceTolerance(pointer<Integer> x,
			pointer<Integer> y, int color, int mx, int my, int radius,
			int tolerance) {
		Point windowloc = ide.connection.getWindowLocation(ide.window);
		Color jcolor = new Color(color);
		mx += windowloc.x;
		my += windowloc.y;
		update_screen(new Rectangle(mx - radius, mx + radius, 2 * radius,
				2 * radius));
		for (int i = 0; i <= radius; i++) {
			Point result = FindColorToleranceInCircleBounded(mx, my, i, null,
					lastCapture, jcolor, tolerance);
			if (result != null) {
				x.set(result.x);
				y.set(result.y);
				return true;
			}
		}
		return false;
	}

	public boolean FindColorSpiralTolerance(pointer<Integer> x,
			pointer<Integer> y, int color, int xs, int ys, int xe, int ye,
			int tolerance) {
		Point windowloc = ide.connection.getWindowLocation(ide.window);
		Color jcolor = new Color(color);
		int x0 = x.get();
		int y0 = y.get();
		x0 += windowloc.x;
		xs += windowloc.x;
		xe += windowloc.x;
		y0 += windowloc.y;
		ys += windowloc.y;
		ye += windowloc.y;
		int xmax = Math.max(Math.abs(x0 - xs), Math.abs(x0 - xe));
		int xmin = Math.min(Math.abs(x0 - xs), Math.abs(x0 - xe));
		int ymax = Math.max(Math.abs(y0 - ys), Math.abs(y0 - ye));
		int ymin = Math.min(Math.abs(y0 - ys), Math.abs(y0 - ye));
		int maxdis = (int) Math.floor(Math.sqrt(ymax * ymax + xmax * xmax));
		int mindis = (int) Math.ceil(Math.sqrt(ymin * ymin + xmin * xmin));
		Rectangle bounds = new Rectangle(xs, ys, xe - xs, ye - ys);
		update_screen(bounds);
		for (int radius = 0; radius <= maxdis; radius++) {
			Point result = FindColorToleranceInCircleBounded(x0, y0, radius,
					radius < mindis ? null : bounds, lastCapture, jcolor,
					tolerance);
			if (result != null) {
				x.set(result.x);
				y.set(result.y);
				return true;
			}
		}
		return false;
	}

	/*
	 * Taken from the wikipedia article :) Thanks, wikipedia!
	 */
	public Point FindColorToleranceInCircleBounded(int x0, int y0, int radius,
			Rectangle bounds, Raster image, Color color, int tolerance) {

		if (isPointInBoundsAndMatchesColorTolerance(x0 + radius, y0, bounds,
				color, image, tolerance)) {
			return new Point(x0 + radius, y0);
		}
		if (isPointInBoundsAndMatchesColorTolerance(x0 - radius, y0, bounds,
				color, image, tolerance)) {
			return new Point(x0 - radius, y0);
		}
		if (isPointInBoundsAndMatchesColorTolerance(x0, y0 + radius, bounds,
				color, image, tolerance)) {
			return new Point(x0, y0 + radius);
		}
		if (isPointInBoundsAndMatchesColorTolerance(x0, y0 - radius, bounds,
				color, image, tolerance)) {
			return new Point(x0, y0 - radius);
		}
		int f = 1 - radius;
		int ddF_x = 1;
		int ddF_y = -2 * radius;
		int x = 0;
		int y = radius;
		while (x < y) {
			// ddF_x == 2 * x + 1;
			// ddF_y == -2 * y;
			// f == x*x + y*y - radius*radius + 2*x - y + 1;
			if (f >= 0) {
				y--;
				ddF_y += 2;
				f += ddF_y;
			}
			x++;
			ddF_x += 2;
			f += ddF_x;
			if (isPointInBoundsAndMatchesColorTolerance(x0 + x, y0 + y, bounds,
					color, image, tolerance))
				return new Point(x0 + x, y0 + y);
			if (isPointInBoundsAndMatchesColorTolerance(x0 - x, y0 + y, bounds,
					color, image, tolerance))
				return new Point(x0 - x, y0 + y);
			if (isPointInBoundsAndMatchesColorTolerance(x0 + x, y0 - y, bounds,
					color, image, tolerance))
				return new Point(x0 + x, y0 - y);
			if (isPointInBoundsAndMatchesColorTolerance(x0 - x, y0 - y, bounds,
					color, image, tolerance))
				return new Point(x0 - x, y0 - y);
			if (isPointInBoundsAndMatchesColorTolerance(x0 + y, y0 + x, bounds,
					color, image, tolerance))
				return new Point(x0 + y, y0 + x);
			if (isPointInBoundsAndMatchesColorTolerance(x0 - y, y0 + x, bounds,
					color, image, tolerance))
				return new Point(x0 - y, y0 + y);
			if (isPointInBoundsAndMatchesColorTolerance(x0 + y, y0 - x, bounds,
					color, image, tolerance))
				return new Point(x0 + y, y0 - x);
			if (isPointInBoundsAndMatchesColorTolerance(x0 - y, y0 - x, bounds,
					color, image, tolerance))
				return new Point(x0 - y, y0 - x);
		}
		return null;
	}

	public boolean isPointInBoundsAndMatchesColorTolerance(int x, int y,
			Rectangle bounds, Color color, Raster image, int tolerance) {
		if (bounds != null && !bounds.contains(x, y)) {
			return false;
		}
		return SimilarColor(color, tolerance, image, x, y);
	}

	public boolean FindWindowTitlePart(String part, boolean casematters) {
		long window = ide.connection.getWindowByNamePart(part, casematters);
		if (window != 0) {
			ide.window = window;
			return true;
		}
		return false;
	}

	public boolean FindWindowBySize(int width, int height) {
		long window = ide.connection.GetWindowBySize(width, height);
		if (window != 0) {
			ide.window = window;
			return true;
		}
		return false;
	}

	public boolean SimilarColors(int color1, int color2, int tolerance) {
		return Math.abs(((color1 & 0x00FF0000) - (color2 & 0x00FF0000)) >> 16)
				+ Math
						.abs(((color1 & 0x0000FF00) - (color2 & 0x0000FF00)) >> 8)
				+ Math.abs((color1 & 0x000000FF) - (color2 & 0x000000FF)) <= tolerance;
	}

	boolean SimilarColor(Color jcolor, int tolerance, Raster data, int x, int y) {
		x -= lastCaptureOffset.x;
		y -= lastCaptureOffset.y;
		int totaloff = 0;
		/*
		 * red
		 */
		totaloff += Math.abs(jcolor.getRed() - data.getSample(x, y, 0));
		if (totaloff > tolerance) {
			return false;
		}
		/*
		 * green
		 */
		totaloff += Math.abs(jcolor.getGreen() - data.getSample(x, y, 1));
		/*
		 * blue
		 */
		totaloff += Math.abs(jcolor.getBlue() - data.getSample(x, y, 2));
		return totaloff <= tolerance;
	}

	boolean ColorsSame(Color color, Raster data, int x, int y) {
		x -= lastCaptureOffset.x;
		y -= lastCaptureOffset.y;
		if (data.getSample(x, y, 0) != color.getRed()) {
			return false;
		}
		if (data.getSample(x, y, 1) != color.getGreen()) {
			return false;
		}
		if (data.getSample(x, y, 2) != color.getBlue()) {
			return false;
		}
		return true;
	}

	public int CountColor(int color, int x1, int y1, int x2, int y2) {
		Point windowloc = ide.connection.getWindowLocation(ide.window);
		Color jcolor = new Color(color);
		x1 += windowloc.x;
		x2 += windowloc.x;
		y1 += windowloc.y;
		y2 += windowloc.y;
		update_screen(new Rectangle(x1, y1, x2 - x1, y2 - y1));
		x1 -= lastCaptureOffset.x;
		y1 -= lastCaptureOffset.y;
		x2 -= lastCaptureOffset.x;
		y2 -= lastCaptureOffset.y;
		int count = 0;
		for (int i = x1; i <= x2; i++) {
			for (int j = y1; j <= y2; j++) {
				if (jcolor.getRed() != lastCapture.getSample(i, j, 0)) {
					continue;
				}
				if (jcolor.getGreen() != lastCapture.getSample(i, j, 1)) {
					continue;
				}
				if (jcolor.getBlue() != lastCapture.getSample(i, j, 2)) {
					continue;
				}
				count++;
			}
		}
		return count;
	}

	public int CountColorTolerance(int color, int x1, int y1, int x2, int y2,
			int tolerance) {
		Point windowloc = ide.connection.getWindowLocation(ide.window);
		Color jcolor = new Color(color);
		x1 += windowloc.x;
		x2 += windowloc.x;
		y1 += windowloc.y;
		y2 += windowloc.y;
		update_screen(new Rectangle(x1, y1, x2 - x1, y2 - y1));
		x1 -= lastCaptureOffset.x;
		y1 -= lastCaptureOffset.y;
		x2 -= lastCaptureOffset.x;
		y2 -= lastCaptureOffset.y;
		int count = 0;
		for (int i = x1; i <= x2; i++) {
			for (int j = y1; j <= y2; j++) {
				if (SimilarColor(jcolor, tolerance, lastCapture, i, j)) {
					count++;
				}
			}
		}
		return count;
	}

	public static void main(String[] arg) {
		scar_robot scar = new scar_robot(null);
		int x = 100;
		int y = 130;
		BufferedImage b = scar.r.createScreenCapture(new Rectangle(x, y, 400,
				300));
		WritableRaster r = b.getRaster();
		int[] lol = r.getPixel(0, 0, (int[]) null);
		System.out.println(lol[0] + " " + lol[1] + " " + lol[2]);
		System.out.println(scar.r.getPixelColor(x, y));
		// ImageIcon icon = new ImageIcon();
		// icon.setImage(new BufferedImage(b.getColorModel(),r,false,null));
		// JOptionPane.showMessageDialog(null, icon);
	}
}
