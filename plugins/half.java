package plugins;

import processing.pascal_plugin;

public class half implements pascal_plugin {

	public static Double half(double d, double d2) {
		return (d2 + d) / 2;
	}

}
