package plugins;

import java.util.LinkedList;

import processing.pascalPlugin;

public class ChangeIt extends pascalPlugin<Object> {
	public String text;

	@SuppressWarnings("unchecked")
	public ChangeIt(LinkedList<Object> arrayOfArgs) throws Exception {
		super(arrayOfArgs);
	}

	@Override
	public Object process() {
		text="OMG!";
		return null;
	}
}
