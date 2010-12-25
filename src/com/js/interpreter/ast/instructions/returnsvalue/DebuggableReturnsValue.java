package com.js.interpreter.ast.instructions.returnsvalue;

import com.js.interpreter.ast.FunctionDeclaration;
import com.js.interpreter.exceptions.ParsingException;
import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.pascaltypes.RuntimeType;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;
import com.js.interpreter.runtime.exception.UnhandledPascalException;

public abstract class DebuggableReturnsValue implements ReturnsValue {

	@Override
	public Object getValue(VariableContext f, RuntimeExecutable<?> main)
			throws RuntimePascalException {
		try {
			return getValueImpl(f, main);
		} catch (RuntimePascalException e) {
			throw e;
		} catch (Exception e) {
			throw new UnhandledPascalException(this.getLineNumber(), e);
		}
	}

	public abstract Object getValueImpl(VariableContext f,
			RuntimeExecutable<?> main) throws RuntimePascalException;

}
