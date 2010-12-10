package com.js.interpreter.pascaltypes;

import java.util.Iterator;

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

	public ReturnsValue convert(Iterator<ReturnsValue> returns_value,
			FunctionDeclaration f) throws ParsingException {
		if (!returns_value.hasNext()) {
			return null;
		}
		ReturnsValue result = returns_value.next();
		RuntimeType other = result.get_type(f);
		if (writable) {
			if (this.equals(other)) {
				return new CreatePointer((VariableAccess) result);
			} else {
				return null;
			}
		}
		return declType.convert(result, f);
	}

	@Override
	public boolean equals(ArgumentType obj) {
		if (obj instanceof RuntimeType) {
			RuntimeType other = (RuntimeType) obj;
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
}
