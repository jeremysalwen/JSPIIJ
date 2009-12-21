package edu.js.interpreter.preprocessed.interpreting_objects.variables;

import edu.js.interpreter.preprocessed.instructions.returns_value.returns_value;

public class returnsvalue_subvar_identifier extends subvar_identifier {
	returns_value value;

	public returnsvalue_subvar_identifier(returns_value next_returns_value) {
		value = next_returns_value;
	}

	@Override
	public boolean isreturnsvalue() {
		return true;
	}
@Override
public String toString() {
	return '['+value.toString()+']';
}
}
