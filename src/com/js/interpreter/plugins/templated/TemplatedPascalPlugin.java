package com.js.interpreter.plugins.templated;

import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.returnsvalue.FunctionCall;
import com.js.interpreter.ast.returnsvalue.ReturnsValue;
import com.js.interpreter.exceptions.ParsingException;
import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.pascaltypes.ArgumentType;
import com.js.interpreter.pascaltypes.DeclaredType;

public interface TemplatedPascalPlugin {
	public String name();

	public FunctionCall generateCall(LineInfo line, ReturnsValue[] values,
			ExpressionContext f) throws ParsingException;

	public FunctionCall generatePerfectFitCall(LineInfo line,
			ReturnsValue[] values, ExpressionContext f) throws ParsingException;

	public ArgumentType[] argumentTypes();

	public DeclaredType return_type();
}
