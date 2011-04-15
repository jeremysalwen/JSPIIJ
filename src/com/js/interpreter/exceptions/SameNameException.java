package com.js.interpreter.exceptions;

import com.js.interpreter.ast.NamedEntity;
import com.js.interpreter.linenumber.LineInfo;

public class SameNameException extends ParsingException {
	public SameNameException(NamedEntity previous, NamedEntity n) {
		super(n.getLineNumber(), n.getEntityType() + " " + n.name()
				+ " conflicts with previously defined "
				+ previous.getEntityType() + " with the same name defined at "
				+ previous.getLineNumber());
	}
}
