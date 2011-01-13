package com.js.interpreter.pascaltypes;

import java.util.Iterator;

import com.js.interpreter.ast.ExpressionContext;
import com.js.interpreter.ast.instructions.returnsvalue.ReturnsValue;
import com.js.interpreter.exceptions.ParsingException;

public interface ArgumentType {
	public ReturnsValue convertArgType(Iterator<ReturnsValue> args,
			ExpressionContext f) throws ParsingException;

	public ReturnsValue perfectFit(Iterator<ReturnsValue> types,
			ExpressionContext e) throws ParsingException;

	@SuppressWarnings("rawtypes")
	public Class getRuntimeClass();
}
