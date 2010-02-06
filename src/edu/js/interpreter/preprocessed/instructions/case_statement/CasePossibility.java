package edu.js.interpreter.preprocessed.instructions.case_statement;

import edu.js.interpreter.preprocessed.instructions.Executable;
import edu.js.interpreter.preprocessed.interpretingobjects.FunctionOnStack;

public class CasePossibility {
	/**
	 * This class represents a line in a case statement.
	 */
	CaseCondition condition;

	Executable[] commands;

	public CasePossibility(CaseCondition condition, Executable[] commands) {
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
	public boolean execute(FunctionOnStack f) {
			for (Executable e : commands) {
				if (e.execute(f)) {
					return true;
				}
			}
		return false;
	}
}
