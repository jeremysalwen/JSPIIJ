package edu.js.interpreter.processing;

import java.util.ArrayList;

public class StaticMethods {

	public static String[] split(String s, String[] regexs) {
		ArrayList<String> result = new ArrayList<String>();
		String total = "";
		for (int i = 0; i < s.length(); i++) {
			boolean toBreak = false;
			for (String s2 : regexs) {
				if (s2.equals(s.substring(i, i + s2.length())))
					toBreak = true;
				if (toBreak)
					break;
			}
			if (toBreak) {
				result.add(total);
				total = "";
			} else
				total += s.charAt(i);
		}
		String[] realResult = new String[result.size()];
		for (int i = 0; i < result.size(); i++)
			realResult[i] = result.get(i);
		return realResult;
	}

	public static String[] splitNoQuotes(String s, String[] split) {
		boolean quotes = false;
		int lastindex = 0;
		ArrayList<String> result = new ArrayList<String>();
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == '\'' || s.charAt(i) == '\"')
				quotes = !quotes;
			if (quotes)
				continue;
			for (String regex : split)
				if (s.substring(i, i + regex.length()).equals(regex)) {
					result.add(s.substring(lastindex, i));
					lastindex = i + regex.length();
					break;
				}
			if (i == s.length() - 1)
				result.add(s.substring(lastindex, i));
		}
		return result.toArray(new String[result.size()]);
	}

	public static int indexOf(Object[] array, Object o) {
		for (int i = 0; i < array.length; i++) {
			if (o.equals(array[i])) {
				return i;
			}
		}
		return -1;
	}
}
