package plugins;

import java.util.ArrayList;

import pascalTypes.pascalType;
import pascalTypes.pascalVar;
import processing.pascalPlugin;

public class ChangeIt extends pascalPlugin<Object> {
	public pascalVar<String> text;

	@SuppressWarnings("unchecked")
	public ChangeIt(ArrayList<pascalType> arrayOfArgs) throws Exception {
		super(arrayOfArgs);
	}

	@Override
	public Object process() {
		text.set("OMG!");
		return null;
	}
}
