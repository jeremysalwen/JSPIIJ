package edu.js.interpreter.preprocessed.instructions;

import edu.js.interpreter.preprocessed.instructions.returnsvalue.ReturnsValue;
import edu.js.interpreter.preprocessed.interpretingobjects.FunctionOnStack;

public class WhileStatement implements Executable {
	ReturnsValue condition;

	Executable command;

	public WhileStatement(ReturnsValue condition, Executable command) {
		this.condition = condition;
		this.command = command;
	}

	public boolean execute(FunctionOnStack f) {
		while ((Boolean) condition.get_value(f)) {
			if(command.execute(f)) {
				break;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "while [" + condition + "] do [" + command + ']';
	}
}
