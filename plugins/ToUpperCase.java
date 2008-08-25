package plugins;

import processing.pascal_plugin;

public class ToUpperCase implements pascal_plugin {
	
	public static String process(String text) {
		return text.toUpperCase();
	}
}
