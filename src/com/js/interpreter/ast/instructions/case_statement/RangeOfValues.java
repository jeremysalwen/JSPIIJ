package com.js.interpreter.ast.instructions.case_statement;

import com.js.interpreter.ast.instructions.returnsvalue.BinaryOperatorEvaluation;
import com.js.interpreter.ast.instructions.returnsvalue.ConstantAccess;
import com.js.interpreter.ast.instructions.returnsvalue.ReturnsValue;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.tokens.OperatorTypes;

public class RangeOfValues implements CaseCondition {
	ReturnsValue lower;

	ReturnsValue higher;

	public RangeOfValues(ReturnsValue lower, ReturnsValue higher) {
		this.lower = lower;
		this.higher = higher;
	}

	public boolean fits(RuntimeExecutable<?> main,VariableContext f, Object value) {
		ConstantAccess access = new ConstantAccess(value);
		BinaryOperatorEvaluation greater_than_lower = new BinaryOperatorEvaluation(
				access, lower, OperatorTypes.GREATEREQ);
		BinaryOperatorEvaluation less_than_higher = new BinaryOperatorEvaluation(
				access, higher, OperatorTypes.LESSEQ);
		return (Boolean) greater_than_lower.get_value(f, main)
				&& (Boolean) less_than_higher.get_value(f, main);
	}
}
