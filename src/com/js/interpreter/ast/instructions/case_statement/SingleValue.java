package com.js.interpreter.ast.instructions.case_statement;

import com.js.interpreter.ast.instructions.returnsvalue.BinaryOperatorEvaluation;
import com.js.interpreter.ast.instructions.returnsvalue.ConstantAccess;
import com.js.interpreter.ast.instructions.returnsvalue.ReturnsValue;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.tokens.OperatorTypes;

public class SingleValue implements CaseCondition {
	ReturnsValue value;

	public SingleValue(ReturnsValue value) {
		this.value = value;
	}

	public boolean fits(RuntimeExecutable<?> main,VariableContext f, Object value) {
		return (Boolean) new BinaryOperatorEvaluation(
				new ConstantAccess(value), this.value, OperatorTypes.EQUALS)
				.get_value(f, main);
	}

}
