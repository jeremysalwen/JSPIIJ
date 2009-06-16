package edu.js.interpreter.preprocessed.interpreting_objects.variables;

public class string_subvar_identifier extends subvar_identifier {
	String s;

	public string_subvar_identifier(String s) {
		this.s = s;
	}

	@Override
	public boolean isreturnsvalue() {
		return false;
	}

}
