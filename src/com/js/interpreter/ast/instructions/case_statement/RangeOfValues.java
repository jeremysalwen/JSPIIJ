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
	LineInfo line;

	public RangeOfValues(ReturnsValue lower, ReturnsValue higher, LineInfo line) {
		this.lower = lower;
		this.higher = higher;
		this.line = line;
	}

	@Override
	public boolean fits(RuntimeExecutable<?> main, VariableContext f,
			Object value) throws RuntimePascalException {
		ConstantAccess access = new ConstantAccess(value, line);
		BinaryOperatorEvaluation greater_than_lower = new BinaryOperatorEvaluation(
				access, lower, OperatorTypes.GREATEREQ, line);
		BinaryOperatorEvaluation less_than_higher = new BinaryOperatorEvaluation(
				access, higher, OperatorTypes.LESSEQ, line);
		return (Boolean) greater_than_lower.get_value(f, main)
				&& (Boolean) less_than_higher.get_value(f, main);
	}
}
