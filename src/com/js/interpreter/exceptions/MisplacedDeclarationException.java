package com.js.interpreter.exceptions;

import com.js.interpreter.ast.expressioncontext.ExpressionContextContract;
import com.js.interpreter.linenumber.LineInfo;

public class MisplacedDeclarationException extends ParsingException {

	public MisplacedDeclarationException(LineInfo line, String declarationType,
			ExpressionContextContract loc) {
		super(line, "Definition of " + declarationType
				+ " is not appropriate here: " + loc);
	}

}
