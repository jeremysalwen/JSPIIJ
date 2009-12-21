package edu.js.appletloader;

import java.applet.Applet;
import java.awt.Insets;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Pack200;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.swing.JFrame;

import serp.bytecode.BCClass;
import serp.bytecode.BCField;
import serp.bytecode.BCMethod;
import serp.bytecode.ConstantInstruction;
import serp.bytecode.Instruction;
import serp.bytecode.MethodInstruction;
import serp.bytecode.Project;
import serp.bytecode.PutFieldInstruction;

public class LardLoader {
	String script;

	public static javawontletmepasspointers getApplet(int world)
			throws Exception {
		String scriptLoc = "http://world" + world + ".runescape.com/";
		String javascript = downloadHTML(new URL(new URL(scriptLoc),
				"plugin.js?param=o0,a0,s0").toExternalForm());
		String regex = "<param name=(\\w+) value=\\\"?([^\\\">]*)\\\"?>";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(javascript);
		while (m.find()) {
			params.put(m.group(1), m.group(2));
		}
		p = Pattern.compile("archive=(.+jar)");
		m = p.matcher(javascript);
		m.find();
		archive = new URL(new URL(scriptLoc), m.group(1));
		p = Pattern.compile("code=(\\w+).class");
		m = p.matcher(javascript);
		m.find();
		String code = m.group(1);
		System.out.println(params);
		System.out.println(archive);
		 URLClassLoader u = new LardClassLoader(new URL[] { archive }, null);
		Applet rs = (Applet) u.loadClass(code).newInstance();
		appletStub a = new appletStub(scriptLoc, scriptLoc.substring(0,
				scriptLoc.indexOf("runescape.com/") + 14), params);
		rs.setStub(a);
		JFrame f = new JFrame();

		f.add(rs);
		Insets in = f.getInsets();
		rs.init();
		a.active = true;
		rs.start();
		f.setSize(in.right + in.left + 770, in.bottom + in.top + 540);
		f.setResizable(false);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
		//f.pack();
		// rs.updateCanvas();
		// rs.addEventFilter();
		javawontletmepasspointers result = new javawontletmepasspointers();
		result.applet = rs;
		result.stub = a;
		return result;
	}

	public void outputReflectionData() throws Exception {
		ToMessup = new LinkedList<String>();
		BCClass StringWrapper = null;
		HashMap<String, BCClass> clases = unpackRunescape(getCacheDir());
		String mousex = null;
		String mousey = null;
		String mouseClicky = null;
		String mouseClickx = null;
		String tab = null;
		long currenttime = System.currentTimeMillis();
		for (Entry<String, BCClass> s : clases.entrySet()) {
			BCClass c = s.getValue();
			if (c.getDeclaredInterfaceNames().length > 0) {
				if (c.getDeclaredMethod("toString") != null) {
					StringWrapper = c;
				}
				BCMethod m = c.getDeclaredMethod("mouseMoved");
				if (m != null) {
					Instruction[] instructions = m.getCode(false)
							.getInstructions();
					for (int j = 0; j < instructions.length; j++) {
						Instruction i = instructions[j];
						if (i instanceof PutFieldInstruction) {
							PutFieldInstruction put = (PutFieldInstruction) i;
							Instruction previous = instructions[j - 1];
							if (previous instanceof MethodInstruction) {
								MethodInstruction call = (MethodInstruction) previous;
								if (call.getMethodName().equals("getX")) {
									mousex = put.getFieldDeclarerName() + "."
											+ put.getFieldName();
								}
								if (call.getMethodName().equals("getY")) {
									mousey = put.getFieldDeclarerName() + "."
											+ put.getFieldName();
								}
							}
						}
					}
					m = c.getDeclaredMethod("mousePressed");
					if (m != null) {
						instructions = m.getCode(false).getInstructions();
						for (int j = 0; j < instructions.length; j++) {
							Instruction i = instructions[j];
							if (i instanceof PutFieldInstruction) {
								PutFieldInstruction put = (PutFieldInstruction) i;
								Instruction previous = instructions[j - 1];
								if (previous instanceof MethodInstruction) {
									MethodInstruction call = (MethodInstruction) previous;
									if (call.getMethodName().equals("getX")) {
										mouseClickx = put
												.getFieldDeclarerName()
												+ "." + put.getFieldName();
									}
									if (call.getMethodName().equals("getY")) {
										mouseClicky = put
												.getFieldDeclarerName()
												+ "." + put.getFieldName();
									}
								}
							}
						}
					}
				}
			}
		}
		BCField bytes = null;
		for (BCField f : StringWrapper.getDeclaredFields()) {
			if (!f.isStatic())
				if (f.getTypeName().equals("[B")) {
					System.out.println("Found the bytes of stringWrapper "
							+ StringWrapper.getName() + "." + f.getName());
					bytes = f;
				}
		}
		BCClass c = clases.get("client");
		int index = 0;
		Instruction[] instructions = null;
		// these are reserved for when we find it
		for (BCMethod m : c.getDeclaredMethods()) {
			instructions = m.getCode(false).getInstructions();
			for (int j = 0; j < instructions.length; j++) {
				Instruction i = instructions[j];
				if (i instanceof ConstantInstruction) {
					ConstantInstruction ldc = (ConstantInstruction) i;
					if (ldc.getType() == String.class
							&& ldc.getStringValue().equals("js5connect")) {
						index = j;
						break;
					}
				}
			}
			if (index != 0)
				break;
		}

		for (int i = index; i < instructions.length; i++) {
			if (instructions[i] instanceof PutFieldInstruction) {
				index = i;
				break;
			}
		}
		PutFieldInstruction loginIndexCaller = (PutFieldInstruction) instructions[index];
		System.out
				.println("took " + (System.currentTimeMillis() - currenttime));
		System.out.println("the login index is "
				+ loginIndexCaller.getFieldDeclarerName() + "."
				+ loginIndexCaller.getFieldName());
		System.out.println("mouse x and y are " + mousex + " " + mousey);
		System.out.println("mouse click x and y are " + mouseClickx + ", "
				+ mouseClicky);
	}

	public static void main(String[] args) throws Exception {
		getApplet(23);
	}

	public static Field getField(String name, ClassLoader c)
			throws ClassNotFoundException, SecurityException,
			NoSuchFieldException, IllegalArgumentException,
			IllegalAccessException {
		String[] s = name.split("\\.");
		Class cl = c.loadClass(s[0]);
		System.out.println(cl.getDeclaredFields()[0].getName());
		Object o = null;
		for (int i = 1; i < s.length; i++) {
			Field f = cl.getDeclaredField(s[i]);
			f.setAccessible(true);
			if (i == s.length - 1) {
				return f;
			}
			o = f.get(o);
			cl = f.getType();
		}
		return null;
	}

	static LinkedList<String> ToMessup;

	static HashMap<String, String> params = new HashMap<String, String>();

	private static URL archive;

	private static String downloadHTML(String address) throws Exception {
		return downloadHTML(address, null);
	}

	private static String downloadHTML(String address, String referer,
			String formData) throws IOException, InterruptedException {
		URL u = new URL(address);
		HttpURLConnection c = (HttpURLConnection) u.openConnection();

		c.setDoInput(true);
		c.setDoOutput(true);
		c.setUseCaches(false);
		c.setRequestMethod("GET");
		c.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		c
				.setRequestProperty(
						"User-Agent",
						"Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.8.0.1) Gecko/20060111 Firefox/1.5.0.1");
		c
				.setRequestProperty("Accept",
						"text/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8");
		c.setRequestProperty("Accept-Language", "en-us,en;q=0.5");
		c.setRequestProperty("Accept-Encoding", "gzip,deflate");
		c.setRequestProperty("Accept-Charset", "UTF-8,*");

		if (referer != null)
			c.setRequestProperty("Referer", referer);

		c.setRequestProperty("Keep-Alive", "300");

		String content = formData;
		c.setRequestProperty("Content-Length", content.length() + "");
		DataOutputStream printout = new DataOutputStream(c.getOutputStream());
		printout.writeBytes(content);
		printout.flush();
		printout.close();
		String contsize = c.getHeaderField("Content-length");
		if (contsize == null)
			contsize = "0";
		byte buffer[] = new byte[Integer.parseInt(contsize)];
		DataInputStream ds = new DataInputStream(c.getInputStream());
		ds.readFully(buffer);
		ds.close();
		Thread.sleep(250 + (int) Math.random() * 250);
		return new String(buffer);
	}

	private static String downloadHTML(String address, String referer)
			throws Exception {
		URL url = new URL(address);
		URLConnection conn = url.openConnection();
		conn
				.setRequestProperty(
						"User-Agent",
						"Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.8.0.1) Gecko/20060111 Firefox/1.5.0.1");
		conn
				.setRequestProperty("Accept",
						"text/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8");
		conn.setRequestProperty("Accept-Language", "en-us,en;q=0.5");
		conn.setRequestProperty("Accept-Encoding", "gzip,deflate");
		conn.setRequestProperty("Accept-Charset", "UTF-8,*");
		if (referer != null)
			conn.setRequestProperty("Referer", referer);
		conn.setRequestProperty("Keep-Alive", "300");
		String contsize = conn.getHeaderField("Content-length");
		if (contsize == null)
			contsize = "0";
		byte buffer[] = new byte[Integer.parseInt(contsize)];
		DataInputStream ds = new DataInputStream(conn.getInputStream());
		ds.readFully(buffer);
		ds.close();
		Thread.sleep(250 + (int) Math.random() * 250);
		return new String(buffer);
	}

	private static HashMap<String, BCClass> unpackRunescape(File jarFile)
			throws Exception {
		int size = (int) jarFile.length();
		RandomAccessFile raf = new RandomAccessFile(jarFile, "rw");
		byte jarFileBuffer[] = new byte[size];

		raf.readFully(jarFileBuffer);
		ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(
				unscrambler(jarFileBuffer)));
		ArrayList<BCClass> ClassGens = new ArrayList<BCClass>(30);
		byte classFileBuffer[] = new byte[1000];
		ZipEntry entry = null;
		Project p = new Project();
		while ((entry = zis.getNextEntry()) != null) {
			String name = entry.getName();
			final String fileExtension = ".class";

			if (!name.endsWith(fileExtension)) {
				continue;
			}
			name = name.replace(fileExtension, "");
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			int readlength = 0;

			while ((readlength = zis.read(classFileBuffer, 0,
					classFileBuffer.length)) != -1) {
				bos.write(classFileBuffer, 0, readlength);
			}
			ClassGens.add(p.loadClass(new ByteArrayInputStream(bos
					.toByteArray())));
		}
		HashMap<String, BCClass> hm = new HashMap<String, BCClass>();

		for (int i = 0; i < ClassGens.size(); i++) {
			hm.put(ClassGens.get(i).getClassName(), ClassGens.get(i));
		}
		return hm;
	}

	private static byte[] unscrambler(byte abyte0[]) throws IOException {
		byte abyte1[] = new byte[2 + abyte0.length];
		ByteArrayOutputStream bytearrayoutputstream;

		abyte1[0] = 31;
		abyte1[1] = -117;
		System.arraycopy(abyte0, 0, abyte1, 2, abyte0.length);
		bytearrayoutputstream = new ByteArrayOutputStream();
		Pack200.newUnpacker().unpack(
				new GZIPInputStream(new ByteArrayInputStream(abyte1)),
				new JarOutputStream(bytearrayoutputstream));
		return bytearrayoutputstream.toByteArray();
	}

	private static File getCacheDir() {
		String[] PossibleLocations = { "c:/rscache/", "/rscache/",
				"c:/windows/", "c:/winnt/", "c:/", "/tmp/", "",
				System.getProperty("user.home") + File.separatorChar };
		String[] PossibleLocationsAdditions = { ".jagex_cache_32",
				".file_store_32" };
		ArrayList<File> matches = new ArrayList<File>(0);

		for (String location : PossibleLocations) {
			for (String addition : PossibleLocationsAdditions) {
				File f = new File(location + addition);
				if (f.exists()) {
					matches.add(f);
				}
			}
		}
		File f = null;
		for (File match : matches) {
			f = new File(match, "runescape/main_file_cache.dat0");
			if (f.exists()) {
				System.out.println(f);
				return f;
			}
		}
		return null;
	}

	private static void packFile(JarFile j, File out) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			GZIPOutputStream gzos = new GZIPOutputStream(baos);
			Pack200.Packer pk = Pack200.newPacker();
			pk.pack(j, gzos);
			gzos.finish();
			if (!out.exists())
				out.createNewFile();
			FileOutputStream fos = new FileOutputStream(out);
			byte[] data = baos.toByteArray();
			fos.write(data, 2, data.length - 2);
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
