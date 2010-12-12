package com.js.interpreter.pascaltypes;

import java.util.Iterator;

import com.js.interpreter.ast.FunctionDeclaration;
import com.js.interpreter.ast.instructions.returnsvalue.ReturnsValue;
import com.js.interpreter.exceptions.ParsingException;

public interface ArgumentType {
	public ReturnsValue convert(Iterator<ReturnsValue> args,
			FunctionDeclaration f) throws ParsingException;

	public Class getRuntimeClass();

	public boolean equals(ArgumentType other);
}