package edu.js.interpreter.pascaltypes;

import edu.js.interpreter.preprocessed.FunctionDeclaration;
import edu.js.interpreter.preprocessed.instructions.returnsvalue.ReturnsValue;
import serp.bytecode.Code;

public abstract class PascalType {
	public abstract Object initialize();

	public abstract boolean isarray();

	public abstract Class toclass();

	public ArrayType get_type_array() {
		return ((ArrayType) this);
	}

	public abstract void get_default_value_on_stack(Code code);

	public abstract ReturnsValue convert(ReturnsValue returns_value,
			FunctionDeclaration f);

}
