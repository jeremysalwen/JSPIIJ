package com.js.interpreter.pascaltypes;

import serp.bytecode.Code;

import com.js.interpreter.ast.ExpressionContext;
import com.js.interpreter.ast.instructions.returnsvalue.ReturnsValue;
import com.js.interpreter.exceptions.ParsingException;

public abstract class DeclaredType {
	public abstract Object initialize();

	public abstract boolean isarray();

	public abstract Class toclass();

	@SuppressWarnings("rawtypes")
	public ArrayType get_type_array() {
		return ((ArrayType) this);
	}

	public abstract ReturnsValue convert(ReturnsValue returns_value,
			ExpressionContext f) throws ParsingException;

	public abstract boolean equals(DeclaredType other);

	public abstract void pushDefaultValue(Code constructor_code);
}
