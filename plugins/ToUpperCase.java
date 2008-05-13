package plugins;

import java.util.ArrayList;
import java.util.LinkedList;

import pascalTypes.pascal_type;
import pascalTypes.standard_type;
import processing.pascalPlugin;

public class ToUpperCase extends pascalPlugin<String> {
	public String text;

	public ToUpperCase(LinkedList<pascal_type> arrayOfArgs) throws Exception {
		super(arrayOfArgs);
	}

	@Override
	public String process() {
		return text.toUpperCase();
	}
}
