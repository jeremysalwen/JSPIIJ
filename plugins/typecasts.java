package plugins;

import processing.pascal_plugin;

public class typecasts implements pascal_plugin {
	public static Integer integer(Double d) {
		return d.intValue();
	}

	public static Double real(Integer i) {
		return i.doubleValue();
	}

	public static String tostring(Integer i) {
		return i.toString();
	}

	public static String tostring(Double d) {
		return d.toString();
	}
}
