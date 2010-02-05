package edu.js.interpreter.preprocessed.instructions.returns_value;

import edu.js.interpreter.pascal_types.pascal_type;
import edu.js.interpreter.preprocessed.function_declaration;
import edu.js.interpreter.preprocessed.interpreting_objects.function_on_stack;
import edu.js.interpreter.tokens.value.operator_types;

public class unary_operator_evaluation implements returns_value {
	public operator_types type;

	public returns_value operon;

	public unary_operator_evaluation(returns_value operon,
			operator_types operator) {
		this.type = operator;
		this.operon = operon;
	}

	public Object get_value(function_on_stack f) {
		Object value = operon.get_value(f);
		Class operon_type = value.getClass();
		return type.operate(value);
	}

	@Override
	public String toString() {
		return "operator [" + type + "] on [" + operon + ']';
	}

	public pascal_type get_type(function_declaration f) {
		return operon.get_type( f);
	}

}
