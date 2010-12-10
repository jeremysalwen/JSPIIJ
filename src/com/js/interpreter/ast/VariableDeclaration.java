package com.js.interpreter.ast;

import java.util.Map;

import com.js.interpreter.pascaltypes.DeclaredType;

public class VariableDeclaration {
	public String name;

	public DeclaredType type;

	public String get_name() {
		return name;
	}

	public VariableDeclaration(String name, DeclaredType type) {
		this.name = name;
		this.type = type;
	}

	public void initialize(Map<String, Object> map) {
		map.put(name, type.initialize());
	}

	@Override
	public int hashCode() {
		return name.hashCode() * 31 + type.hashCode();
	}
}
