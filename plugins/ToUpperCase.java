package plugins;

import java.util.ArrayList;

import pascalTypes.pascalType;
import processing.pascalPlugin;

public class ToUpperCase extends pascalPlugin<String> {
	public String text;

	public ToUpperCase(ArrayList<pascalType> arrayOfArgs) throws Exception {
		super(arrayOfArgs);
	}

	@Override
	public String process() {
		return text.toUpperCase();
	}
}
