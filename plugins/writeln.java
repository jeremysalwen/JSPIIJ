package plugins;

import java.util.ArrayList;

import pascalTypes.pascalType;
import processing.pascalPlugin;

public class writeln extends pascalPlugin<Object> {
	public String text;

	// put args above here, remember, no fields besides args

	@SuppressWarnings("unchecked")
	public writeln(ArrayList<pascalType> arrayOfArgs) throws Exception {
		super(arrayOfArgs);
	}

	public Object process() {
		System.out.println(text);
		return null;
	}

}
