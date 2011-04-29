package com.js.interpreter.ast.instructions.case_statement;

import com.js.interpreter.ast.returnsvalue.BinaryOperatorEvaluation;
import com.js.interpreter.ast.returnsvalue.ConstantAccess;
import com.js.interpreter.ast.returnsvalue.ReturnsValue;
import com.js.interpreter.exceptions.ParsingException;
import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;
import com.js.interpreter.tokens.OperatorTypes;

public class RangeOfValues implements CaseCondition {
	Object lower;

	Object higher;
	LineInfo line;

	public RangeOfValues(Object lower, Object higher, LineInfo line) {
		this.lower = lower;
		this.higher = higher;
		this.line = line;
	}

	@Override
	public boolean fits(Object value) throws RuntimePascalException {
		ConstantAccess access = new ConstantAccess(value, line);
		ConstantAccess low = new ConstantAccess(lower, line);
		ConstantAccess hi = new ConstantAccess(higher, line);
		BinaryOperatorEvaluation greater_than_lower = new BinaryOperatorEvaluation(
				access, low, OperatorTypes.GREATEREQ, line);
		BinaryOperatorEvaluation less_than_higher = new BinaryOperatorEvaluation(
				access, hi, OperatorTypes.LESSEQ, line);
		return (Boolean) greater_than_lower.getValue(null, null)
				&& (Boolean) less_than_higher.getValue(null, null);
	}

	@Override
	public LineInfo getLineNumber() {
		return line;
	}
}
