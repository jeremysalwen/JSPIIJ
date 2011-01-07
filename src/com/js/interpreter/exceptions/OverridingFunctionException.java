package com.js.interpreter.exceptions;

import com.js.interpreter.ast.AbstractFunction;
import com.js.interpreter.ast.FunctionDeclaration;
import com.js.interpreter.linenumber.LineInfo;

public class OverridingFunctionException extends ParsingException {

	public OverridingFunctionException(FunctionDeclaration old, LineInfo line) {
		super(line, "Redfining function body for " + old.toString()
				+ "which was previous define at " + old.line);
	}

	public OverridingFunctionException(AbstractFunction old,
			FunctionDeclaration news) {
		super(news.line, "Attempting to override plugin definition"
				+ old.toString());
	}
}
