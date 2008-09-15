package plugins;

import processing.pascal_plugin;

public class standard_io implements pascal_plugin {
	/**
	 * Redirects to System.out
	 * @param s The string to output.
	 */
	public static void writeln(String s) {
		System.out.println(s);
	}
}
