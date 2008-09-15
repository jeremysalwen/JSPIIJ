package preprocessed.instructions.case_statement;

import preprocessed.instructions.returns_value.binary_operator_evaluation;
import preprocessed.instructions.returns_value.constant_access;
import preprocessed.instructions.returns_value.returns_value;
import preprocessed.interpreting_objects.function_on_stack;
import tokens.value.operator_types;

public class range_of_values implements case_condition {
	returns_value lower;

	returns_value higher;

	public range_of_values(returns_value lower, returns_value higher) {
		this.lower = lower;
		this.higher = higher;
	}

	public boolean fits(function_on_stack f, Object value) {
		constant_access access = new constant_access(value);
		binary_operator_evaluation greater_than_lower = new binary_operator_evaluation(
				access, lower, operator_types.GREATEREQ);
		binary_operator_evaluation less_than_higher = new binary_operator_evaluation(
				access, higher, operator_types.LESSEQ);
		return (Boolean) greater_than_lower.get_value(f)
				&& (Boolean) less_than_higher.get_value(f);
	}
}
