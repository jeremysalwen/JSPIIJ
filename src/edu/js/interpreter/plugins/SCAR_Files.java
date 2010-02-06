package edu.js.interpreter.plugins;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import edu.js.interpreter.gui.Ide;
import edu.js.interpreter.preprocessed.interpretingobjects.Pointer;
import edu.js.interpreter.processing.PascalPlugin;

public class SCAR_Files implements PascalPlugin {
	Ide ide;

	HashMap<Integer, RandomAccessFile> open_files;

	int filecounter = 0;

	public SCAR_Files(Ide i) {
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
		if (ide.settings.hasFileAccess(location)) {
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

	public boolean readfilebyte(int filenum, Pointer<Byte> out) {
		RandomAccessFile f = open_files.get(filenum);
		try {
			out.set(f.readByte());
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	public boolean readfileint(int filenum, Pointer<Integer> out) {
		RandomAccessFile f = open_files.get(filenum);
		try {
			out.set(f.readInt());
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	public boolean readfile(int filenum, Pointer<String> out, int length) {
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

	public String ExtractFileName(String name) {
		return new File(name).getName();
	}

	public String ExtractPathName(String name) {
		return new File(name).getParent();
	}

	public String ExtractFileExt(String name) {
		return name.substring(name.lastIndexOf('.') + 1);
	}

	public String MD5FromFile(String filename) {
		MessageDigest digest;
		DigestInputStream is = null;
		try {
			try {
				digest = MessageDigest.getInstance("MD5");

				try {
					is = new DigestInputStream(new FileInputStream(filename),
							digest);
					while (is.available() > 0) {
						is.read();
					}
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return new BigInteger(1, digest.digest()).toString(16);
			} catch (NoSuchAlgorithmException e2) {
				e2.printStackTrace();
			}
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.err.println("Some sort of error trying to md5sum a file");
		return null;
	}
}
