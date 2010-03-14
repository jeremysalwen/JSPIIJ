package edu.js.interpreter.preprocessed.instructions.returnsvalue;

import edu.js.interpreter.pascaltypes.PascalType;
import edu.js.interpreter.preprocessed.FunctionDeclaration;
import edu.js.interpreter.preprocessed.interpretingobjects.FunctionOnStack;
import edu.js.interpreter.preprocessed.interpretingobjects.variables.SubvarIdentifier;

public class CreatePointer implements ReturnsValue {
	VariableAccess container;

	SubvarIdentifier index;

	public CreatePointer(VariableAccess container, SubvarIdentifier index) {
		this.container = container;
		this.index = index;
	}

	public PascalType get_type(FunctionDeclaration f) {
		PascalType container_type = container.get_type(f);
		return index.getType(container_type);
	}

	public Object get_value(FunctionOnStack f) {
		Object value = container.get_value(f);
		return index.create_pointer(value, f);
	}
}
