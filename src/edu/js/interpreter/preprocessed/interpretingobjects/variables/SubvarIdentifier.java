package edu.js.interpreter.preprocessed.interpretingobjects.variables;

import edu.js.interpreter.preprocessed.instructions.returnsvalue.ReturnsValue;

public abstract class SubvarIdentifier {
	public abstract boolean isreturnsvalue();

	public boolean isstring() {
		return !isreturnsvalue();
	}

	public String string() {
		return ((String_SubvarIdentifier) this).s;
	}

	public ReturnsValue returnsvalue() {
		return ((ReturnsValue_SubvarIdentifier) this).value;
	}
}
