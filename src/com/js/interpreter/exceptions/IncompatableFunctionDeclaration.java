package com.js.interpreter.exceptions;

import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.pascaltypes.DeclaredType;

public class IncompatableFunctionDeclaration extends ParsingException {

	public IncompatableFunctionDeclaration(LineInfo line,
			DeclaredType returntype, DeclaredType previousreturntype) {
		super(line, "Function declaration declares conflicting return type "
				+ returntype + ".  It previously was defined as "
				+ previousreturntype);
	}

}
