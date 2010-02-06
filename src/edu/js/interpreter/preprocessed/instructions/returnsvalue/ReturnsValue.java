package edu.js.interpreter.preprocessed.instructions.returnsvalue;

import edu.js.interpreter.pascaltypes.PascalType;
import edu.js.interpreter.preprocessed.FunctionDeclaration;
import edu.js.interpreter.preprocessed.interpretingobjects.FunctionOnStack;

public interface ReturnsValue {
	public abstract Object get_value(FunctionOnStack f);

	public abstract PascalType get_type(FunctionDeclaration f);
}
