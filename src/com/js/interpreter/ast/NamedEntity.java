package com.js.interpreter.ast;

import com.js.interpreter.linenumber.LineInfo;

public interface NamedEntity {
	public LineInfo getLineNumber();

	public String getEntityType();

	public String name();
}
