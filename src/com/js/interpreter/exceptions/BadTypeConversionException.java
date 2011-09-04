package com.js.interpreter.exceptions;

import com.js.interpreter.ast.returnsvalue.ReturnsValue;
import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.pascaltypes.DeclaredType;

public class BadTypeConversionException extends ParsingException {

	public BadTypeConversionException(ReturnsValue obj,
			DeclaredType out, DeclaredType in, boolean implicit) {
		super(obj.getLineNumber(), "The expression " + obj + " is of type " + in
				+ ", which cannot be " + (implicit ? "implicitly " : "")
				+ "converted to to the type " + in+".");

	}
}
