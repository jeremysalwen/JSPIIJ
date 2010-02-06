package edu.js.SCARlib;

import java.awt.Point;

public interface SCARLibInterface {

	long UserFindWindow();

	long getWindowByName(String name);

	long getRootWindow();

	void ActivateWindow(long window);

	Point GetWindowDimensions(long window);

	public Point getWindowLocation(long window);

	long getWindowByNamePart(String name, boolean casematters);

	long GetWindowBySize(int width, int height);
}
