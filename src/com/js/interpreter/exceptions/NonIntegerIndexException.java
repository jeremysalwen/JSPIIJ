package com.js.interpreter.exceptions;

import com.js.interpreter.ast.returnsvalue.ReturnsValue;

public class NonIntegerIndexException extends ParsingException {
	ReturnsValue value;

	public NonIntegerIndexException(ReturnsValue value) {
		super(value.getLineNumber());
		this.value = value;
	}

	@Override
	public String getMessage() {
		return "Array indexes must be integers: " + value;
	}
}
