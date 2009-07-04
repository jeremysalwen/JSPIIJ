package edu.js.interpreter.plugins;

import java.applet.Applet;
import java.awt.Canvas;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.PixelGrabber;
import java.util.Random;

import edu.js.appletloader.LardLoader;
import edu.js.interpreter.gui.ide;
import edu.js.interpreter.processing.pascal_plugin;

public class scar_STUPID_interface implements pascal_plugin {
	Applet a;

	MouseListener[] clicklisteners;

	boolean button1down;

	boolean button2down;

	public scar_STUPID_interface(ide i) {
		this.a = i.client;
	}

	public void MoveMouse(int x, int y) {
		MouseMotionListener[] motionlisteners = a.getMouseMotionListeners();
		for (MouseMotionListener m : motionlisteners) {
			if (!button1down && !button2down) {
				m.mouseMoved(new MouseEvent(a,

				MouseEvent.MOUSE_MOVED, System.currentTimeMillis(), 0, x, y, 0,
						false));
			} else {
				m.mouseDragged(new MouseEvent(a,

				MouseEvent.MOUSE_DRAGGED, System.currentTimeMillis(), 0, x, y,
						0, false));
			}
		}
	}

	public void ClickMouse(int x, int y, boolean leftmouse) {
		MouseListener[] mouslisteners = a.getMouseListeners();
		for (MouseListener m : mouslisteners) {
			m.mouseClicked(new MouseEvent(a, MouseEvent.MOUSE_CLICKED, System
					.currentTimeMillis(),
					leftmouse ? MouseEvent.BUTTON1_DOWN_MASK
							: MouseEvent.BUTTON2_DOWN_MASK, x, y, 1, false));
		}
	}

	public void HoldMouse(int x, int y, boolean leftmouse) {
		MouseListener[] mouslisteners = a.getMouseListeners();
		for (MouseListener m : mouslisteners) {
			m.mouseClicked(new MouseEvent(a, MouseEvent.MOUSE_PRESSED, System
					.currentTimeMillis(),
					leftmouse ? MouseEvent.BUTTON1_DOWN_MASK
							: MouseEvent.BUTTON2_DOWN_MASK, x, y, 1, false));
		}
		if (leftmouse) {
			button1down = true;
		} else {
			button2down = true;
		}
	}

	public void ReleaseMouse(int x, int y, boolean leftmouse) {
		MouseListener[] mouslisteners = a.getMouseListeners();
		for (MouseListener m : mouslisteners) {
			m.mouseReleased(new MouseEvent(a, MouseEvent.MOUSE_RELEASED, System
					.currentTimeMillis(),
					leftmouse ? MouseEvent.BUTTON1_DOWN_MASK
							: MouseEvent.BUTTON2_DOWN_MASK, x, y, 1, false));
		}
		if (leftmouse) {
			button1down = false;
		} else {
			button2down = false;
		}
	}

	public int GetColor(int x, int y) {
		return -1;
	}

	public static void main(String[] args) {
		try {
			Applet a = LardLoader.getApplet(29).applet;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
