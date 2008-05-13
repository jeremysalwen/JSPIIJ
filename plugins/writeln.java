package plugins;

import java.util.ArrayList;
import java.util.LinkedList;

import pascalTypes.pascal_type;
import pascalTypes.standard_type;
import processing.pascalPlugin;

public class writeln extends pascalPlugin<Object> {
	public String text;

	// put args above here, remember, no fields besides args

	@SuppressWarnings("unchecked")
	public writeln(LinkedList<pascal_type> arrayOfArgs) throws Exception {
		super(arrayOfArgs);
	}

	public Object process() {
		System.out.println(text);
		return null;
	}

}
