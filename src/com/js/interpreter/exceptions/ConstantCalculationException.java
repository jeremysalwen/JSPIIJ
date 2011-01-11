package com.js.interpreter.exceptions;

import com.js.interpreter.runtime.exception.RuntimePascalException;

public class ConstantCalculationException extends ParsingException {
	RuntimePascalException e;

	public ConstantCalculationException(RuntimePascalException e) {
		super(e.line, "Error while computing constant value: " + e.getMessage());
		this.e = e;
	}
}
