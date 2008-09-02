package preprocessed.instructions.case_statement;

import preprocessed.instructions.returns_value.binary_operator_evaluation;
import preprocessed.instructions.returns_value.constant_access;
import preprocessed.instructions.returns_value.returns_value;
import preprocessed.interpreting_objects.function_on_stack;
import tokens.operator_types;

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
