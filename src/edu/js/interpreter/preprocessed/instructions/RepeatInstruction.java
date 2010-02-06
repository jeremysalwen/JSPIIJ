package edu.js.interpreter.preprocessed.instructions;

import edu.js.interpreter.preprocessed.instructions.returnsvalue.ReturnsValue;
import edu.js.interpreter.preprocessed.interpretingobjects.FunctionOnStack;

public class RepeatInstruction implements Executable {
	Executable command;

	ReturnsValue condition;

	public RepeatInstruction(Executable command, ReturnsValue condition) {
		this.command = command;
		this.condition = condition;
	}

	public boolean execute(FunctionOnStack f) {
		do {
			if (command.execute(f)) {
				break;
			}
		} while (((Boolean) condition.get_value(f)));
		return false;
	}

}
