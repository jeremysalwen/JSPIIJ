package com.js.interpreter.exceptions;

import com.js.interpreter.ast.NamedEntity;
import com.js.interpreter.linenumber.LineInfo;

public class SameNameException extends ParsingException {
	public SameNameException(LineInfo line, NamedEntity previous,
			NamedEntity n, String name) {
		super(line, n.getEntityType() + " " + name + " defined at "
				+ n.getLineNumber() + " conflicts with previously defined "
				+ previous.getEntityType() + " with the same name defined at "
				+ previous.getLineNumber());
	}
}
