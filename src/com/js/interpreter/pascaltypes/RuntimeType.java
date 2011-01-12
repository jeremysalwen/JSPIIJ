package com.js.interpreter.pascaltypes;

import java.util.Iterator;

import com.js.interpreter.ast.ExpressionContext;
import com.js.interpreter.ast.FunctionDeclaration;
import com.js.interpreter.ast.instructions.returnsvalue.ReturnsValue;
import com.js.interpreter.ast.instructions.returnsvalue.VariableAccess;
import com.js.interpreter.ast.instructions.returnsvalue.boxing.CreatePointer;
import com.js.interpreter.exceptions.ParsingException;
import com.js.interpreter.runtime.VariableBoxer;

public class RuntimeType implements ArgumentType {
	public final DeclaredType declType;
	public final boolean writable;

	public RuntimeType(DeclaredType declType, boolean writable) {
		this.declType = declType;
		this.writable = writable;
	}

	public ReturnsValue convert(ReturnsValue value, ExpressionContext f)
			throws ParsingException {

		RuntimeType other = value.get_type(f);
		if (writable) {
			if (this.equals(other)) {
				return new CreatePointer((VariableAccess) value);
			} else {
				return null;
			}
		}
		return declType.convert(value, f);
	}

	public boolean equals(RuntimeType obj) {
		if (obj instanceof RuntimeType) {
			RuntimeType other = obj;
			return other.writable == this.writable
					&& this.declType.equals(other.declType);
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return (writable ? "" : "non-") + "writable " + declType.toString();
	}

	@Override
	public Class<?> getRuntimeClass() {
		if (writable) {
			return VariableBoxer.class;
		} else {
			return declType.toclass();
		}
	}

	@Override
	public ReturnsValue convertArgType(Iterator<ReturnsValue> args,
			ExpressionContext f) throws ParsingException {
		if (!args.hasNext()) {
			return null;
		}
		return convert(args.next(), f);
	}

}
