package com.js.interpreter.exceptions;

import com.js.interpreter.ast.instructions.returnsvalue.ReturnsValue;
import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.pascaltypes.DeclaredType;
import com.js.interpreter.runtime.variables.VariableIdentifier;

public class IncorrectTypeAssignment extends ParsingException {
	public IncorrectTypeAssignment(LineInfo line, VariableIdentifier varname,
			DeclaredType vartype, ReturnsValue value, DeclaredType valueType) {
		super(line, "Could not convert '" + value + "' of type " + valueType
				+ " to type " + vartype + " for assignment to variable '"
				+ varname + "'");
	}
}
