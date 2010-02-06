package edu.js.interpreter.preprocessed.instructions.returnsvalue;

import edu.js.interpreter.pascaltypes.JavaClassBasedType;
import edu.js.interpreter.pascaltypes.PascalType;
import edu.js.interpreter.preprocessed.FunctionDeclaration;
import edu.js.interpreter.preprocessed.interpretingobjects.FunctionOnStack;

public class ConstantAccess implements ReturnsValue {
	final Object constant_value;

	public ConstantAccess(Object o) {
		this.constant_value = o;
	}

	public Object get_value(FunctionOnStack f) {
		return constant_value;
	}

	@Override
	public String toString() {
		return "get_constant[" + constant_value + ']';
	}

	public PascalType get_type(FunctionDeclaration f) {
		return JavaClassBasedType.anew(constant_value.getClass());
	}
}
