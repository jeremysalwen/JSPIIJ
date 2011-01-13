package com.js.interpreter.ast.instructions.returnsvalue;

import com.js.interpreter.ast.CompileTimeContext;
import com.js.interpreter.ast.ExpressionContext;
import com.js.interpreter.exceptions.ParsingException;
import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.pascaltypes.RuntimeType;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;
import com.js.interpreter.runtime.variables.VariableIdentifier;

public class VariableAccess extends DebuggableReturnsValue {
	public VariableIdentifier variable_name;
	LineInfo line;

	public VariableAccess(VariableIdentifier name, LineInfo line) {
		this.variable_name = name;
		this.line = line;
	}

	@Override
	public LineInfo getLineNumber() {
		return line;
	}

	@Override
	public Object getValueImpl(VariableContext f, RuntimeExecutable<?> main)
			throws RuntimePascalException {
		return variable_name.get_value(f, main);
	}

	@Override
	public String toString() {
		return variable_name.toString();
	}

	@Override
	public RuntimeType get_type(ExpressionContext f) throws ParsingException {
		return variable_name.get_type(f);
	}

	@Override
	public Object compileTimeValue(CompileTimeContext context)
			throws ParsingException {
		return null;
	}
}
