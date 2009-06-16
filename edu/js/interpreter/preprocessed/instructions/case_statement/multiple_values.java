package edu.js.interpreter.preprocessed.instructions.case_statement;

import edu.js.interpreter.preprocessed.instructions.returns_value.binary_operator_evaluation;
import edu.js.interpreter.preprocessed.instructions.returns_value.constant_access;
import edu.js.interpreter.preprocessed.instructions.returns_value.returns_value;
import edu.js.interpreter.preprocessed.interpreting_objects.function_on_stack;
import edu.js.interpreter.tokens.value.operator_types;

public class multiple_values implements case_condition {
	returns_value[] values;

	public multiple_values(returns_value[] values) {
		this.values = values;
	}

	public boolean fits(function_on_stack f, Object value) {
		constant_access access = new constant_access(value);
		for (returns_value v : values) {
			if ((Boolean) new binary_operator_evaluation(v, access,
					operator_types.EQUALS).get_value(f)) {
				return true;
			}
		}
		return false;
	}

}
