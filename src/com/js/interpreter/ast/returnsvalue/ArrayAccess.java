package com.js.interpreter.ast.returnsvalue;

import java.lang.reflect.Array;

import com.js.interpreter.ast.CompileTimeContext;
import com.js.interpreter.ast.ExpressionContext;
import com.js.interpreter.ast.instructions.SetArray;
import com.js.interpreter.ast.instructions.SetValueExecutable;
import com.js.interpreter.exceptions.ParsingException;
import com.js.interpreter.exceptions.UnassignableTypeException;
import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.pascaltypes.ArrayType;
import com.js.interpreter.pascaltypes.RuntimeType;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public class ArrayAccess extends DebuggableReturnsValue {
	ReturnsValue container;
	ReturnsValue index;

	public ArrayAccess(ReturnsValue container, ReturnsValue index) {
		this.container = container;
		this.index = index;
	}

	@Override
	public RuntimeType get_type(ExpressionContext f) throws ParsingException {
		RuntimeType r = (container.get_type(f));
		return new RuntimeType(((ArrayType<?>) r.declType).element_type,
				r.writable);
	}

	@Override
	public LineInfo getLineNumber() {
		return index.getLineNumber();
	}

	@Override
	public Object compileTimeValue(CompileTimeContext context)
			throws ParsingException {
		Object cont = container.compileTimeValue(context);
		Object ind = index.compileTimeValue(context);
		if (ind == null || cont == null) {
			return null;
		} else {
			return Array.get(cont, ((Integer) ind));
		}
	}

	@Override
	public SetValueExecutable createSetValueInstruction(ReturnsValue r)
			throws UnassignableTypeException {
		return new SetArray(container, index, r);
	}

	@Override
	public Object getValueImpl(VariableContext f, RuntimeExecutable<?> main)
			throws RuntimePascalException {
		Object cont = container.getValue(f, main);
		Object ind = index.getValue(f, main);
		return Array.get(cont, ((Integer) ind));
	}

	@Override
	public ReturnsValue compileTimeExpressionFold(CompileTimeContext context) throws ParsingException {
		return new ArrayAccess(container.compileTimeExpressionFold(context),
				index.compileTimeExpressionFold(context));
	}

}
