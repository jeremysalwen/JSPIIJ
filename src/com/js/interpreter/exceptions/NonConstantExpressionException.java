package com.js.interpreter.exceptions;

import com.js.interpreter.ast.returnsvalue.ReturnsValue;

public class NonConstantExpressionException extends ParsingException {
	public NonConstantExpressionException(ReturnsValue value) {
		super(value.getLineNumber(), "The expression \"" + value
				+ "\" is not constant.");
	}
}
