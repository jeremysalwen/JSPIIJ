package bcel_test;

import preprocessed.interpreting_objects.variables.contains_variables;

public class point implements contains_variables {

	double x;
	int y;


	public Object get_var(String name) {
		name = name.intern();
		if (name == "x") {
			return x;
		}
		if (name == "y") {
			return y;
		}
		return null;
	}

	public void set_var(String name, Object val) {
		if (name.equals("x")) {
			x = (Double) val;
		}
		if (name.equals("y")) {
			y = (Integer) val;
		}
	}

}
