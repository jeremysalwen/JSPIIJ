package plugins;

import java.util.LinkedList;

import processing.pascalPlugin;

public class ToUpperCase extends pascalPlugin<String> {
	public String text;

	public ToUpperCase(LinkedList<Object> arrayOfArgs) throws Exception {
		super(arrayOfArgs);
	}

	@Override
	public String process() {
		return text.toUpperCase();
	}
}
