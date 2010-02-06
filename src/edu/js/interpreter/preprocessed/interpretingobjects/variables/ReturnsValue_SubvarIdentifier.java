package edu.js.interpreter.preprocessed.interpretingobjects.variables;

import edu.js.interpreter.preprocessed.instructions.returnsvalue.ReturnsValue;

public class ReturnsValue_SubvarIdentifier extends SubvarIdentifier {
	ReturnsValue value;

	public ReturnsValue_SubvarIdentifier(ReturnsValue next_returns_value) {
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
