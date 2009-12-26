package edu.js.interpreter.preprocessed.instructions.returns_value;

import edu.js.interpreter.pascal_types.pascal_type;
import edu.js.interpreter.preprocessed.function_declaration;
import edu.js.interpreter.preprocessed.interpreting_objects.function_on_stack;

public interface returns_value {
	public abstract Object get_value(function_on_stack f);

	public abstract pascal_type get_type(function_declaration f);
}
