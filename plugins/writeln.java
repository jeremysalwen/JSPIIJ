package plugins;

import java.util.LinkedList;

import processing.pascalPlugin;

public class writeln extends pascalPlugin<Object> {
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
