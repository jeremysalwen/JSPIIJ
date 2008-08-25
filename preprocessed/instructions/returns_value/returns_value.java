package preprocessed.instructions.returns_value;

import preprocessed.function_declaration;
import preprocessed.interpreting_objects.function_on_stack;
import processing.pascal_program;

public interface returns_value {
	public abstract Object get_value(function_on_stack f);

	public abstract Class get_type(pascal_program p, function_declaration f);
}
