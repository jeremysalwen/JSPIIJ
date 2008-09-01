package plugins;

import processing.pascal_plugin;

public class standard_io implements pascal_plugin {
	public static void writeln(String s) {
		System.out.println(s);
	}
}
