package plugins;

import java.util.LinkedList;

import processing.pascal_plugin;

public class ToUpperCase extends pascal_plugin<String> {
	public String text;

	public ToUpperCase(LinkedList<Object> arrayOfArgs) throws Exception {
		super(arrayOfArgs);
	}

	@Override
	public String process() {
		return text.toUpperCase();
	}
}
