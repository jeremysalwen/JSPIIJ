package edu.js.interpreter.plugins;

import java.awt.Point;
import java.awt.Toolkit;

import edu.js.interpreter.gui.ide;
import edu.js.interpreter.preprocessed.interpreting_objects.pointer;
import edu.js.interpreter.processing.pascal_plugin;

public class scar_mouse_keyboard implements pascal_plugin {
	ide ide;

	public scar_mouse_keyboard(ide i) {
		this.ide = i;
	}

	public void getmousepos(pointer<Integer> x, pointer<Integer> y) {
		Point loc = java.awt.MouseInfo.getPointerInfo().getLocation();
		x.set(loc.x - ide.windowloc.x);
		y.set(loc.y - ide.windowloc.x);
	}
}
