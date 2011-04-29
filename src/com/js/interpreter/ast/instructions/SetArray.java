package com.js.interpreter.ast.instructions;

import java.lang.reflect.Array;

import com.js.interpreter.ast.CompileTimeContext;
import com.js.interpreter.ast.returnsvalue.ReturnsValue;
import com.js.interpreter.exceptions.ParsingException;
import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public class SetArray implements SetValueExecutable {
	ReturnsValue container;
	ReturnsValue index;
	ReturnsValue val;

	public SetArray(ReturnsValue container, ReturnsValue index, ReturnsValue val) {
		this.container = container;
		this.index = index;
		this.val = val;
	}

	@Override
	public LineInfo getLineNumber() {
		return index.getLineNumber();
	}

	@Override
	public ExecutionResult execute(VariableContext f, RuntimeExecutable<?> main)
			throws RuntimePascalException {
		Object cont = container.getValue(f, main);
		Object ind = index.getValue(f, main);
		Object v = val.getValue(f, main);
		Array.set(cont, (Integer) ind, v);
		return ExecutionResult.NONE;
	}

	@Override
	public void setAssignedValue(ReturnsValue value) {
		this.val = value;
	}

	@Override
	public SetValueExecutable compileTimeConstantTransform(CompileTimeContext c)
			throws ParsingException {
		return this;
	}

}
