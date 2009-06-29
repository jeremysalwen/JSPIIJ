package edu.js.interpreter.preprocessed.instructions;

import edu.js.interpreter.preprocessed.interpreting_objects.function_on_stack;

public interface executable {
	/*
	 * This returns a value to indicate if it is "breaking" out of a loop.
	 */
	public boolean execute(function_on_stack f);
}
