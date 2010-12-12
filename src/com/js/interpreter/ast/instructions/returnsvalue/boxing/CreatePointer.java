package com.js.interpreter.ast.instructions.returnsvalue.boxing;

import com.js.interpreter.ast.FunctionDeclaration;
import com.js.interpreter.ast.instructions.returnsvalue.ReturnsValue;
import com.js.interpreter.ast.instructions.returnsvalue.VariableAccess;
import com.js.interpreter.exceptions.ParsingException;
import com.js.interpreter.pascaltypes.RuntimeType;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.variables.SubvarIdentifier;

public class CreatePointer implements ReturnsValue {
	VariableAccess container;

	SubvarIdentifier index;

	/**
	 * Destructively generates a CreatePointer object from a variableAccess.
	 * 
	 * @param a
	 */
	public CreatePointer(VariableAccess a) {
		index = a.variable_name.get(a.variable_name.size() - 1);
		a.variable_name.remove(a.variable_name.size() - 1);
		if (a.variable_name.size() > 0) {
			container = a;
		}
	}

	public RuntimeType get_type(FunctionDeclaration f) throws ParsingException{
		if (container == null) {
			return new RuntimeType(f.get_variable_type(index.toString()), true);
		} else {
			RuntimeType container_type = container.get_type(f);
			return new RuntimeType(index.getType(container_type.declType),
					container_type.writable);
		}

	}

	public Object get_value(VariableContext f, RuntimeExecutable<?> main) {
		Object value;
		if (container == null) {
			value = f;
		} else {
			value = container.get_value(f, main);
		}
		return index.create_pointer(value, f,main);
	}
}