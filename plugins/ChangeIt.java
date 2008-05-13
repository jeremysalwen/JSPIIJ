package plugins;

import java.util.ArrayList;

import pascalTypes.pascal_type;
import pascalTypes.standard_type;
import pascalTypes.standard_var;
import processing.pascalPlugin;

public class ChangeIt extends pascalPlugin<Object> {
	public standard_var<String> text;

	@SuppressWarnings("unchecked")
	public ChangeIt(ArrayList<pascal_type> arrayOfArgs) throws Exception {
		super(arrayOfArgs);
	}

	@Override
	public Object process() {
		text.set("OMG!");
		return null;
	}
}
