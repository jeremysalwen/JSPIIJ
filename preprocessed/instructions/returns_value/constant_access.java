package preprocessed.instructions.returns_value;

import preprocessed.interpreting_objects.function_on_stack;

public class constant_access implements returns_value {
	final Object constant_value;

	public constant_access(Object o) {
		this.constant_value = o;
	}

	@Override
	public Object get_value(function_on_stack f) {
		return constant_value;
	}

	@Override
	public String toString() {
		return "get_constant[" + constant_value + ']';
	}
}
