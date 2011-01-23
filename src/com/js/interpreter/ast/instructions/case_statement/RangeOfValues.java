package com.js.interpreter.ast.instructions.case_statement;

import com.js.interpreter.ast.instructions.returnsvalue.BinaryOperatorEvaluation;
import com.js.interpreter.ast.instructions.returnsvalue.ConstantAccess;
import com.js.interpreter.ast.instructions.returnsvalue.ReturnsValue;
import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;
import com.js.interpreter.tokens.OperatorTypes;

public class RangeOfValues implements CaseCondition {
	ReturnsValue lower;

	ReturnsValue higher;

	public RangeOfValues(ReturnsValue lower, ReturnsValue higher) {
		this.lower = lower;
		this.higher = higher;
	}

	@Override
	public boolean fits(RuntimeExecutable<?> main, VariableContext f,
			Object value) throws RuntimePascalException {
		ConstantAccess access = new ConstantAccess(value, lower.getLineNumber());
		BinaryOperatorEvaluation greater_than_lower = new BinaryOperatorEvaluation(
				access, lower, OperatorTypes.GREATEREQ, lower.getLineNumber());
		BinaryOperatorEvaluation less_than_higher = new BinaryOperatorEvaluation(
				access, higher, OperatorTypes.LESSEQ, lower.getLineNumber());
		return (Boolean) greater_than_lower.getValueImpl(f, main)
				&& (Boolean) less_than_higher.getValueImpl(f, main);
	}

	@Override
	public LineInfo getLineNumber() {
		return lower.getLineNumber();
	}
}
