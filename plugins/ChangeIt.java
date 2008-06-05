package plugins;

import java.util.LinkedList;

import processing.pascal_plugin;

public class ChangeIt extends pascal_plugin<Object> {
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
