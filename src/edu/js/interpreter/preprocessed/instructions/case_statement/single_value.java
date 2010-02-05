package edu.js.interpreter.preprocessed.instructions.case_statement;

import edu.js.interpreter.preprocessed.instructions.returns_value.binary_operator_evaluation;
import edu.js.interpreter.preprocessed.instructions.returns_value.constant_access;
import edu.js.interpreter.preprocessed.instructions.returns_value.returns_value;
import edu.js.interpreter.preprocessed.interpreting_objects.function_on_stack;
import edu.js.interpreter.tokens.value.operator_types;

public class single_value implements case_condition {
	returns_value value;

	public single_value(returns_value value) {
		this.value = value;
	}

	public boolean fits(function_on_stack f, Object value) {
		return (Boolean) new binary_operator_evaluation(new constant_access(
				value), this.value, operator_types.EQUALS).get_value(f);
	}

}
