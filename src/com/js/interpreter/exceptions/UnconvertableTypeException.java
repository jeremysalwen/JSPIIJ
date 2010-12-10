package com.js.interpreter.exceptions;

import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.pascaltypes.DeclaredType;

public class UnconvertableTypeException extends ParsingException {
	public UnconvertableTypeException(LineInfo line, DeclaredType from,
			DeclaredType to) {
		super(line, "Cannot convert from type " + from + " to type " + to);
	}
}
