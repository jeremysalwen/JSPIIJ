package edu.js.interpreter.preprocessed.instructions;

import edu.js.interpreter.preprocessed.interpretingobjects.FunctionOnStack;

public interface Executable {
	/*
	 * This returns a value to indicate if it is "breaking" out of a loop.
	 */
	public boolean execute(FunctionOnStack f);
}
