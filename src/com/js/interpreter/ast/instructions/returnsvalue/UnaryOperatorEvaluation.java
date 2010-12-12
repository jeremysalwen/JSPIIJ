package com.js.interpreter.ast.instructions.returnsvalue;

import javax.naming.OperationNotSupportedException;

import com.js.interpreter.ast.FunctionDeclaration;
import com.js.interpreter.exceptions.ParsingException;
import com.js.interpreter.pascaltypes.RuntimeType;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.tokens.OperatorTypes;

public class UnaryOperatorEvaluation implements ReturnsValue {
	public OperatorTypes type;

	public ReturnsValue operon;

	public UnaryOperatorEvaluation(ReturnsValue operon, OperatorTypes operator) {
		this.type = operator;
		this.operon = operon;
	}

	public Object get_value(VariableContext f, RuntimeExecutable<?> main) {
		try {
			Object value = operon.get_value(f, main);
			return type.operate(value);
		} catch (OperationNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String toString() {
		return "operator [" + type + "] on [" + operon + ']';
	}

	public RuntimeType get_type(FunctionDeclaration f) throws ParsingException {
		return operon.get_type(f);
	}

}
