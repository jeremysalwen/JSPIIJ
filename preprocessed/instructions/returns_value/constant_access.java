package preprocessed.instructions.returns_value;

import pascal_types.custom_type;
import pascal_types.custom_type_declaration;
import pascal_types.pascal_type;
import preprocessed.function_declaration;
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

	@Override
	public pascal_type get_type(function_declaration f) {
		if (constant_value instanceof custom_type) {
			return new pascal_type(((custom_type) constant_value).type);
		} else {
			return new pascal_type(constant_value.getClass());
		}
	}
}
