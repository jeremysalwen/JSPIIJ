package edu.js.interpreter.packagedtypes;

import edu.js.interpreter.preprocessed.interpreting_objects.variables.contains_variables;

public class TPoint implements contains_variables {
	public int x;

	public int y;

	public TPoint() {
		this.x = 0;
		this.y = 0;
	}

	public Object get_var(String name) {
		if (name.equals("x")) {
			return x;
		}
		if (name.equals("y")) {
			return y;
		}
		return null;
	}

	public void set_var(String name, Object val) {
		if (name.equals("x")) {
			x = (Integer) val;
		}
		if (name.equals("y")) {
			y = (Integer) val;
		}
		System.err.println("Tried to put to unexistant struct member");
	}

	@Override
	public contains_variables clone() {
		TPoint result = new TPoint();
		result.x = this.x;
		result.y = this.y;
		return result;
	}
}
