package edu.js.interpreter.preprocessed.instructions;

import edu.js.interpreter.preprocessed.instructions.returns_value.returns_value;
import edu.js.interpreter.preprocessed.interpreting_objects.function_on_stack;

public class while_statement implements executable {
	returns_value condition;

	executable command;

	public while_statement(returns_value condition, executable command) {
		this.condition = condition;
		this.command = command;
	}

	public boolean execute(function_on_stack f) {
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
