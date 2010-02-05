package edu.js.interpreter.preprocessed.instructions.case_statement;

import edu.js.interpreter.preprocessed.instructions.executable;
import edu.js.interpreter.preprocessed.interpreting_objects.function_on_stack;

public class case_possibilty {
	/**
	 * This class represents a line in a case statement.
	 */
	case_condition condition;

	executable[] commands;

	public case_possibilty(case_condition condition, executable[] commands) {
		this.condition = condition;
		this.commands = commands;
	}

	/**
	 * Executes the contained commands in this branch.
	 * 
	 * @param value
	 *            the value being examined in this case statement.
	 * @return Whether or not it has broken.
	 */
	public boolean execute(function_on_stack f) {
			for (executable e : commands) {
				if (e.execute(f)) {
					return true;
				}
			}
		return false;
	}
}
