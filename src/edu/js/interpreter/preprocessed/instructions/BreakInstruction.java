package edu.js.interpreter.preprocessed.instructions;

import edu.js.interpreter.preprocessed.interpretingobjects.FunctionOnStack;

public class BreakInstruction implements Executable {

	public boolean execute(FunctionOnStack f) {
		return true;
	}

}
