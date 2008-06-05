package plugins;

import java.util.LinkedList;

import processing.pascal_plugin;

public class writeln extends pascal_plugin<Object> {
	public String text;

	// put args above here, remember, no fields besides args

	@SuppressWarnings("unchecked")
	public writeln(LinkedList<Object> arrayOfArgs) throws Exception {
		super(arrayOfArgs);
	}

	public Object process() {
		System.out.println(text);
		return null;
	}

}
