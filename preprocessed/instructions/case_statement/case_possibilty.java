package preprocessed.instructions.case_statement;

import preprocessed.instructions.executable;
import preprocessed.interpreting_objects.function_on_stack;

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
	 * If the condition is true for the passed value, it executes the commands.
	 * 
	 * @param value
	 *            the value being examined in this case statement.
	 * @return Whether or not it has broken.
	 */
	// TODO this must be fixed. I need a break system!
	public boolean execute(function_on_stack f, Object value) {
		if (condition.fits(f, value)) {
			for (executable e : commands) {
				if (e.execute(f)) {
					return true;
				}
			}
		}
		return false;
	}
}
