package edu.js.interpreter.plugins;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import sun.security.provider.MD5;
import edu.js.interpreter.preprocessed.interpreting_objects.pointer;
import edu.js.interpreter.processing.pascal_plugin;

public class scar_strings implements pascal_plugin {
	public static void main(String[] args) {
		String input = "Hello World";
		System.out.println(md5(input));
	}

	public static String between(String s1, String s2, String s) {
		int startindex = s.indexOf(s1) + s1.length();
		int endindex = s.indexOf(s2, startindex);
		return s.substring(startindex, endindex);
	}

	public static String capitalize(String s) {
		boolean lastSpace = true;
		char[] chars = s.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if (lastSpace) {
				chars[i] = Character.toUpperCase(chars[i]);
				lastSpace = false;
			}
			if (chars[i] == ' ') {
				lastSpace = true;
			}
		}
		return new String(chars);
	}

	public static String copy(String s, int ifrom, int count) {
		return s.substring(ifrom, ifrom + count);
	}

	public static void delete(pointer<String> s, int ifrom, int count) {
		String input = s.get();
		s.set(input.substring(0, ifrom - 1)
				+ input.substring(ifrom + count - 1));
	}

	public static boolean endswith(String suffix, String tosearch) {
		return tosearch.endsWith(suffix);
	}

	public static void findregexp() {
		// TODO there is no documentation on the freddy1990 wiki
	}

	public static String getletters(String s) {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (Character.isLetter(c)) {
				result.append(c);
			}
		}
		return s.toString();
	}

	public static String getnumbers(String s) {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (Character.isDigit(c)) {
				result.append(c);
			}
		}
		return s.toString();
	}

	public static String getothers(String s) {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (!Character.isDigit(c) && !Character.isLetter(c)) {
				result.append(c);
			}
		}
		return s.toString();
	}

	public static boolean InStrArr(String Str, String[] Arr,
			boolean casesensitive) {
		for (String s : Arr) {
			if (casesensitive ? s.equals(Str) : s.equalsIgnoreCase(Str)) {
				return true;
			}
		}
		return false;
	}

	public static void insert() {
		// TODO no doc
	}

	public static void LastPosEx() {
		// TODO no doc
	}

	public static void LastPos() {
		// TODO no doc
	}

	public static void Left() {
		// TODO no doc
	}

	public static int length(String s) {
		return s.length(); // TODO double check this is correct.
	}

	public static String md5(String s) {
		try {
			MessageDigest digester = MessageDigest.getInstance("MD5");
			digester.update(s.getBytes());
			return new BigInteger(1, digester.digest()).toString(16);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public static void padl() {
		// TODO no doc
	}

	public static void padr() {
		// TODO no doc
	}

	public static void padz() {
		// TODO no doc
	}

	public static void posex() {
		// TODO no doc
	}

	public static void pos() {
		// TODO no doc
	}

	public static void regexpos() {
		// TODO no doc
	}

	public static void replaceregex() {
		// TODO no doc
	}

	public static void replace() {
		// TODO no doc
	}

	public static void replicate() {
		// TODO no doc
	}

	public static void right() {
		// TODO no doc
	}

	public static void setlength() {
		// TODO no doc
	}

	public static void startswith() {
		// TODO no doc
	}

	public static void strget() {
		// TODO no doc
	}

	public static void strset() {
		// TODO no doc
	}

	public static void stringofchar() {
		// TODO no doc
	}
	public static void trimex() {
		// TODO no doc
	}
	public static void trim() {
		// TODO no doc
	}
	public static void trimletters() {
		// TODO no doc
	}
	public static void trimnumbers() {
		// TODO no doc
	}
	public static void trimothers() {
		// TODO no doc
	}
	public static void uppercase() {
		// TODO no doc
	}
}
