package edu.js.interpreter.preprocessed.instructions.case_statement;

import edu.js.interpreter.preprocessed.interpreting_objects.function_on_stack;

public interface case_condition {
	public boolean fits(function_on_stack f,Object value);
}
