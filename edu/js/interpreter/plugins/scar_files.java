package edu.js.interpreter.plugins;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.HashMap;

import edu.js.interpreter.gui.ide;
import edu.js.interpreter.processing.pascal_plugin;

public class scar_files implements pascal_plugin {
	ide ide;

	HashMap<Integer, RandomAccessFile> open_files;

	int filecounter = 0;

	public scar_files(ide i) {
		ide = i;
		open_files = new HashMap<Integer, RandomAccessFile>();
	}

	public boolean FileExists(String s) {
		File f = new File(s);
		return f.exists() && !f.isDirectory();
	}

	public boolean DirectoryExists(String DirectoryName) {
		File f = new File(DirectoryName);
		return f.exists() && f.isDirectory();
	}

	public int openfile(String location, boolean shared) {
		File f = new File(location);
		if (ide.settings.hasAccess(location)) {
			RandomAccessFile openedfile = null;
			try {
				openedfile = new RandomAccessFile(f, "rw");
			} catch (Exception e) {
				try {
					if (shared) {
						openedfile = new RandomAccessFile(f, "r");
					}
				} catch (Exception e1) {
				}
			}
			if (openedfile == null) {
				return -1;
			}
			open_files.put(filecounter, openedfile);
			return filecounter++;
		} else {
			return -1;
		}
	}
}
