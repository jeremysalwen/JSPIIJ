package com.js.interpreter.exceptions;

import com.js.interpreter.ast.FunctionDeclaration;
import com.js.interpreter.linenumber.LineInfo;

public class OverridingFunctionException extends ParsingException {

	public OverridingFunctionException(LineInfo line, FunctionDeclaration f) {
		super(line, "Multiple definitions of the same function: "
				+ f.toString());
	}

}
