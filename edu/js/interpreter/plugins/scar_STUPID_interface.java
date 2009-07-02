package edu.js.interpreter.plugins;

import java.applet.Applet;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Random;

import edu.js.interpreter.gui.ide;
import edu.js.interpreter.processing.pascal_plugin;

public class scar_STUPID_interface implements pascal_plugin {
	Applet a;

	public scar_STUPID_interface(ide i) {
		this.a = i.client;
	}

	public void MoveMouse(int x, int y) {
		((MouseMotionListener) a).mouseMoved(new MouseEvent(a, new Random()
				.nextInt(), System.currentTimeMillis(), 0, x, y, 0, false));
	}
	public void ClickMouse(int x, int y, boolean leftmouse) {
		((MouseListener)a).mouseClicked(new MouseEvent(a,new Random().nextInt(),System.currentTimeMillis(),))
	}
}
