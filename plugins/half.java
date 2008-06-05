package plugins;

import java.util.LinkedList;

import processing.pascal_plugin;

public class half extends pascal_plugin<Double> {
	public double d;
	public double d2;

	public half(LinkedList<Object> arrayOfArgs) throws Exception {
		super(arrayOfArgs);
	}

	@Override
	public Double process() {
		return (d2 + d) / 2;
	}

}
