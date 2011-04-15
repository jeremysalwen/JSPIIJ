package com.js.interpreter.exceptions;

import com.js.interpreter.linenumber.LineInfo;

public class MultipleDefaultValuesException extends ParsingException {

	public MultipleDefaultValuesException(LineInfo line) {
		super(line,
				"Default Values can only be used when declaring variables one at a time.");
	}

}
