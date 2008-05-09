package plugins;

import java.util.ArrayList;

import pascalTypes.pascalType;
import processing.pascalPlugin;

public class half extends pascalPlugin<Double> {
	public double d;
	public double d2;

	public half(ArrayList<pascalType> arrayOfArgs) throws Exception {
		super(arrayOfArgs);
	}

	@Override
	public Double process() {
		return (d2 + d) / 2;
	}

}
