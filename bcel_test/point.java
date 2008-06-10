package bcel_test;

import preprocessed.interpreting_objects.variables.contains_variables;

public class point implements contains_variables {

	int x;
	int y;
	
	@Override
	public Object get_var(String name) {
		if (name.equals("x")) {
			return x;
		}
		if (name.equals("y")) {
			return y;
		}
		return null;
	}

	@Override
	public void set_var(String name, Object val) {
		if (name.equals("x")) {
			x = (Integer) val;
		}
		if (name.equals("y")) {
			y = (Integer) val;
		}
	}

}
