package plugins;

import processing.pascal_plugin;

public class writeln implements pascal_plugin {

	public static void Writeln(String text) {
		System.out.println(text);
	}

}
