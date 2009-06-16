package edu.js.interpreter.preprocessed.instructions;

import edu.js.interpreter.preprocessed.instructions.returns_value.binary_operator_evaluation;
import edu.js.interpreter.preprocessed.instructions.returns_value.constant_access;
import edu.js.interpreter.preprocessed.instructions.returns_value.returns_value;
import edu.js.interpreter.preprocessed.instructions.returns_value.variable_access;
import edu.js.interpreter.preprocessed.interpreting_objects.function_on_stack;
import edu.js.interpreter.preprocessed.interpreting_objects.variables.variable_identifier;
import edu.js.interpreter.tokens.value.operator_types;

public class for_statement implements executable {
	variable_identifier temp_var;

	returns_value first;

	returns_value last;

	executable command;

	public for_statement(variable_identifier temp_var,
			returns_value first, returns_value last, executable command) {
		this.temp_var = temp_var;
		this.first = first;
		this.last = last;
		this.command = command;
	}

	public boolean execute(function_on_stack f) {
		constant_access last_value = new constant_access(last.get_value(f));
		variable_access get_temp_var = new variable_access(temp_var);
		new variable_set(temp_var, first).execute(f);
		binary_operator_evaluation less_than_last = new binary_operator_evaluation(
				get_temp_var, last_value, operator_types.LESSEQ);
		variable_set increment_temp = new variable_set(temp_var,
				new binary_operator_evaluation(get_temp_var,
						new constant_access(1), operator_types.PLUS));

		while (((Boolean) less_than_last.get_value(f)).booleanValue()) {
			if(command.execute(f)) {
				break;
			}
			increment_temp.execute(f);
		}
		return false;
	}
}
