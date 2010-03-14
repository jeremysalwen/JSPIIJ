package edu.js.interpreter.preprocessed.instructions.returnsvalue;

import edu.js.interpreter.pascaltypes.JavaClassBasedType;
import edu.js.interpreter.pascaltypes.PascalType;
import edu.js.interpreter.preprocessed.FunctionDeclaration;
import edu.js.interpreter.preprocessed.interpretingobjects.FunctionOnStack;
import edu.js.interpreter.preprocessed.interpretingobjects.variables.String_SubvarIdentifier;
import edu.js.interpreter.preprocessed.interpretingobjects.variables.SubvarIdentifier;
import edu.js.interpreter.preprocessed.interpretingobjects.variables.VariableIdentifier;

public class VariableAccess implements ReturnsValue {
	public VariableIdentifier variable_name;

	public VariableAccess(VariableIdentifier name) {
		this.variable_name = name;
	}

	public Object get_value(FunctionOnStack f) {
		return f.get_var(variable_name);
	}

	@Override
	public String toString() {
		return "get_variable[" + variable_name + ']';
	}

	public PascalType get_type(FunctionDeclaration f) {
		SubvarIdentifier ident = variable_name.get(0);
		PascalType type = f.get_variable_type(ident.toString());
		if (type == null) {
			type = f.program.main.get_variable_type(ident.toString());
		}
		for (int i = 1; i < variable_name.size(); i++) {
			type = variable_name.get(i).getType(type);
		}
		assert (type != null);
		return type;
	}
}
