package edu.js.interpreter.plugins;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Random;

import edu.js.interpreter.gui.ide;
import edu.js.interpreter.preprocessed.interpreting_objects.pointer;
import edu.js.interpreter.processing.pascal_plugin;

public class scar_robot implements pascal_plugin {
	ide ide;

	Robot r;

	Random rand;

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

	public void ClickMouse(int x, int y) {
		Point windowloc = ide.connection.getWindowLocation(ide.window);
		x += windowloc.x;
		y += windowloc.y;
		long mouseclickduration = Math.max(
				(long) (rand.nextGaussian() * 20) + 100, 1);
		if (!MouseInfo.getPointerInfo().getLocation().equals(new Point(x, y))) {
			r.mouseMove(x, y);
		}
		r.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		try {
			Thread.sleep(mouseclickduration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		r.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
	}

	public void HoldMouse(int x, int y, boolean left) {
		Point windowloc = ide.connection.getWindowLocation(ide.window);
		x += windowloc.x;
		y += windowloc.y;
		int buttonmask = left ? InputEvent.BUTTON1_DOWN_MASK
				: InputEvent.BUTTON2_DOWN_MASK;
		if (!MouseInfo.getPointerInfo().getLocation().equals(new Point(x, y))) {
			r.mouseMove(x, y);
		}
		r.mousePress(buttonmask);
	}

	public void ReleaseMouse(int x, int y, boolean left) {
		Point windowloc = ide.connection.getWindowLocation(ide.window);
		x += windowloc.x;
		y += windowloc.y;
		int buttonmask = left ? InputEvent.BUTTON1_DOWN_MASK
				: InputEvent.BUTTON2_DOWN_MASK;
		if (!MouseInfo.getPointerInfo().getLocation().equals(new Point(x, y))) {
			r.mouseMove(x, y);
		}
		r.mouseRelease(buttonmask);
	}

	public void SendKeys(String tosend) {
		for (char c : tosend.toCharArray()) {
			GenerateKeyTyped(c);
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

	public void GenerateKeyTyped(char c) {
		if (needsShift(c)) {
			r.keyPress(KeyEvent.VK_SHIFT);
		}
		r.keyPress(GetKeyCode(c));
		r.keyRelease(GetKeyCode(c));
		if (needsShift(c)) {
			r.keyRelease(KeyEvent.VK_SHIFT);
		}
	}

	public void KeyDown(int key) {
		r.keyPress(key);
	}

	public void KeyUp(int key) {
		r.keyRelease(key);
	}

	public static void main(String[] args) {
		scar_robot scar = new scar_robot(null);
		scar
				.SendKeys("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890-=!@#$%^&*()_+<>?:\"{}|");
	}
}
