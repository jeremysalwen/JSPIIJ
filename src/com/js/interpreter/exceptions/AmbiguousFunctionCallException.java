package com.js.interpreter.exceptions;

import com.js.interpreter.ast.AbstractFunction;
import com.js.interpreter.ast.instructions.returnsvalue.FunctionCall;
import com.js.interpreter.linenumber.LineInfo;

public class AmbiguousFunctionCallException extends ParsingException {

	public AmbiguousFunctionCallException(LineInfo line, AbstractFunction possible,
			AbstractFunction alternative) {
		super(line, "Ambigious function call could be interpreted as "
				+ possible + " or as " + alternative);
	}

}
