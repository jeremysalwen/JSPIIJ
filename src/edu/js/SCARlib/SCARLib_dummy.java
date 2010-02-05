package edu.js.SCARlib;

import java.awt.Point;

/**
 * This class is a dummy so that pascalinterpreterinjava will not depend on
 * SCARlib. Really just a quick hack so the whole thing can be packaged in one
 * piece. However, this makes the SCAR functions not work in the interpreter.
 * Not that anyone would notice...
 * 
 * @author jeremy
 * 
 */
public class SCARLib_dummy implements SCARLib_interface {

	@Override
	public void ActivateWindow(long window) {
	}

	@Override
	public long GetWindowBySize(int width, int height) {
		return 0;
	}

	@Override
	public Point GetWindowDimensions(long window) {
		return null;
	}

	@Override
	public long UserFindWindow() {
		return 0;
	}

	@Override
	public long getRootWindow() {
		return 0;
	}

	@Override
	public long getWindowByName(String name) {
		return 0;
	}

	@Override
	public long getWindowByNamePart(String name, boolean casematters) {
		return 0;
	}

	@Override
	public Point getWindowLocation(long window) {
		return null;
	}

}
