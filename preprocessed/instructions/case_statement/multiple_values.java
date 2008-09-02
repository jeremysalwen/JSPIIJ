package preprocessed.instructions.case_statement;

import preprocessed.instructions.returns_value.binary_operator_evaluation;
import preprocessed.instructions.returns_value.constant_access;
import preprocessed.instructions.returns_value.returns_value;
import preprocessed.interpreting_objects.function_on_stack;
import tokens.operator_types;

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
