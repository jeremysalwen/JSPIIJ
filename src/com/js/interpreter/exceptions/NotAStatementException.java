package com.js.interpreter.exceptions;

import com.js.interpreter.ast.instructions.returnsvalue.ReturnsValue;

public class NotAStatementException extends ParsingException {

	public NotAStatementException(ReturnsValue r) {
		super(r.getLineNumber(), r + " is not an instruction by itself.");
	}

}
