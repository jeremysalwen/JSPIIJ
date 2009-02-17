package preprocessed.interpreting_objects.variables;

import preprocessed.instructions.returns_value.returns_value;

public abstract class subvar_identifier {
	public abstract boolean isreturnsvalue();

	public boolean isstring() {
		return !isreturnsvalue();
	}

	public String string() {
		return ((string_subvar_identifier) this).s;
	}

	public returns_value returnsvalue() {
		return ((returnsvalue_subvar_identifier) this).value;
	}
}
