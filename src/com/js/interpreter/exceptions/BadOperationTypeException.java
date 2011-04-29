package com.js.interpreter.exceptions;

import com.js.interpreter.ast.returnsvalue.ReturnsValue;
import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.pascaltypes.DeclaredType;
import com.js.interpreter.tokens.OperatorTypes;

public class BadOperationTypeException extends ParsingException {
	public BadOperationTypeException() {
		super(new LineInfo(-1, "Unknown"));
	}

	public BadOperationTypeException(LineInfo line, DeclaredType t1,
			DeclaredType t2, ReturnsValue v1, ReturnsValue v2,
			OperatorTypes operation) {
		super(line, "Operator " + operation
				+ " cannot be applied to arguments '" + v1 + "' and '" + v2
				+ "'.  One has type " + t1 + " and the other has type " + t2
				+ ".");
	}

	public BadOperationTypeException(LineInfo line, OperatorTypes operator) {
		super(line, "Operator " + operator + " is not a unary operator.");
	}
}
