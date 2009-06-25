package edu.js.interpreter.plugins;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.HashMap;

import edu.js.interpreter.gui.ide;
import edu.js.interpreter.preprocessed.interpreting_objects.pointer;
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

	public int openfileex(String location, boolean shared, String mode) {
		File f = new File(location);
		if (ide.settings.hasAccess(location)) {
			RandomAccessFile openedfile = null;
			try {
				openedfile = new RandomAccessFile(f, mode);
			} catch (Exception e) {
				try {
					if (shared) {
						openedfile = new RandomAccessFile(f, mode);
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

	public int openfile(String location, boolean shared) {
		return openfileex(location, shared, "r");

	}

	public int rewritefile(String location, boolean shared) {
		int result = openfileex(location, shared, "w");
		try {
			open_files.get(result).setLength(0);
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		}
		return result;
	}

	public void closefile(int filenum) {
		try {
			open_files.get(filenum).close();
		} catch (Exception e) {
		}
	}

	public boolean endoffile(int filenum) {
		RandomAccessFile f = open_files.get(filenum);
		try {
			return f.getFilePointer() >= f.length();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public long filesize(int filenum) {
		RandomAccessFile f = open_files.get(filenum);
		try {
			return f.length();
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		}
	}

	public boolean readfilebyte(int filenum, pointer<Byte> out) {
		RandomAccessFile f = open_files.get(filenum);
		try {
			out.set(f.readByte());
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	public boolean readfileint(int filenum, pointer<Integer> out) {
		RandomAccessFile f = open_files.get(filenum);
		try {
			out.set(f.readInt());
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	public boolean readfile(int filenum, pointer<String> out, int length) {
		RandomAccessFile f = open_files.get(filenum);
		try {
			byte[] buffer = new byte[length];
			f.read(buffer);
			out.set(new String(buffer));
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	public boolean writefilebyte(int filenum, byte b) {
		RandomAccessFile f = open_files.get(filenum);
		try {
			f.writeByte(b);
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	public boolean writefileint(int filenum, int i) {
		RandomAccessFile f = open_files.get(filenum);
		try {
			f.writeInt(i);
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	public boolean writefilestring(int filenum, String s) {
		RandomAccessFile f = open_files.get(filenum);
		try {
			f.write(s.getBytes());
			return true;
		} catch (IOException e) {
			return false;
		}
	}
}
