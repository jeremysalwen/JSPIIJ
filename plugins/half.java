package plugins;

import java.util.ArrayList;
import java.util.LinkedList;

import pascalTypes.pascal_type;
import pascalTypes.standard_type;
import processing.pascalPlugin;

public class half extends pascalPlugin<Double> {
	public double d;
	public double d2;

	public half(LinkedList<pascal_type> arrayOfArgs) throws Exception {
		super(arrayOfArgs);
	}

	@Override
	public Double process() {
		return (d2 + d) / 2;
	}

}
