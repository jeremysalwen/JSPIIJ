package com.js.interpreter.exceptions;

import com.js.interpreter.ast.returnsvalue.ReturnsValue;

public class UnassignableTypeException extends ParsingException {

	public UnassignableTypeException(ReturnsValue value) {
		super(value.getLineNumber(), "The expression " + value
				+ " cannot have a value assigned to it.");
	}

}
