package com.js.interpreter.ast;

import com.js.interpreter.linenumber.LineInfo;
import com.sun.org.apache.bcel.internal.classfile.LineNumber;

public class ConstantDefinition implements NamedEntity {
	Object value;
	LineInfo line;

	public ConstantDefinition(Object value, LineInfo line) {
		this.value = value;
		this.line = line;
	}

	public Object getValue() {
		return value;
	}

	public LineInfo getLineNumber() {
		return line;
	}

	@Override
	public String getEntityType() {
		return "constant";
	}
}
