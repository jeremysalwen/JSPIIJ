package preprocessed.instructions.returns_value;

import preprocessed.function_declaration;
import preprocessed.interpreting_objects.function_on_stack;
import processing.pascal_program;

public class constant_access implements returns_value {
	final Object constant_value;

	public constant_access(Object o) {
		this.constant_value = o;
	}

	public Object get_value(function_on_stack f) {
		return constant_value;
	}

	@Override
	public String toString() {
		return "get_constant[" + constant_value + ']';
	}

	public Class get_type(pascal_program p, function_declaration f) {
		return constant_value.getClass();
	}
}
