package com.js.interpreter.ast.instructions.returnsvalue.boxing;

import com.js.interpreter.ast.ExpressionContext;
import com.js.interpreter.ast.FunctionDeclaration;
import com.js.interpreter.ast.instructions.returnsvalue.DebuggableReturnsValue;
import com.js.interpreter.ast.instructions.returnsvalue.VariableAccess;
import com.js.interpreter.exceptions.ParsingException;
import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.pascaltypes.RuntimeType;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;
import com.js.interpreter.runtime.variables.SubvarIdentifier;

public class CreatePointer extends DebuggableReturnsValue {
	VariableAccess container;

	SubvarIdentifier index;
	LineInfo line;

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
		this.line = a.getLineNumber();
	}

	@Override
	public LineInfo getLineNumber() {
		return line;
	}

	@Override
	public RuntimeType get_type(ExpressionContext f) throws ParsingException {
		if (container == null) {
			return new RuntimeType(f.getVariableType(index.toString()), true);
		} else {
			RuntimeType container_type = container.get_type(f);
			return new RuntimeType(index.getType(container_type.declType),
					container_type.writable);
		}

	}

	@Override
	public Object getValueImpl(VariableContext f, RuntimeExecutable<?> main)
			throws RuntimePascalException {
		Object value;
		if (container == null) {
			value = f;
		} else {
			value = container.getValueImpl(f, main);
		}
		return index.create_pointer(value, f, main);
	}

	@Override
	public Object compileTimeValue() throws ParsingException {
		Object cont=container.compileTimeValue();
		if(cont!=null) {
			return null;
		}
		return null;
	}
}
