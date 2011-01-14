package com.js.interpreter.ast;

import java.util.Map;

import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.pascaltypes.DeclaredType;

public class VariableDeclaration implements NamedEntity {
	public String name;

	public DeclaredType type;

	LineInfo line;

	public String get_name() {
		return name;
	}

	public VariableDeclaration(String name, DeclaredType type, LineInfo line) {
		this.name = name;
		this.type = type;
		this.line = line;
	}

	public void initialize(Map<String, Object> map) {
		map.put(name, type.initialize());
	}

	@Override
	public int hashCode() {
		return name.hashCode() * 31 + type.hashCode();
	}

	@Override
	public String getEntityType() {
		return "variable";
	}

	@Override
	public LineInfo getLineNumber() {
		return line;
	}
}
