package edu.js.interpreter.plugins;

import java.io.File;
import java.util.HashMap;

import edu.js.interpreter.gui.ide;
import edu.js.interpreter.processing.pascal_plugin;

public class scar_files implements pascal_plugin {
	ide ide;

	HashMap<Integer, File> open_files;

	int fielcounter = 0;

	public scar_files(ide i) {
		ide = i;
		open_files = new HashMap<Integer, File>();
	}

	public boolean FileExists(String s) {
		File f = new File(s);
		return f.exists() && !f.isDirectory();
	}
	public boolean DirectoryExists(String DirectoryName) {
		File f = new File(DirectoryName);
		return f.exists() && f.isDirectory();
	}
}
