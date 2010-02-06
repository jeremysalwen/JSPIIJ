package edu.js.interpreter.preprocessed.instructions.case_statement;

import edu.js.interpreter.preprocessed.instructions.returnsvalue.BinaryOperatorEvaluation;
import edu.js.interpreter.preprocessed.instructions.returnsvalue.ConstantAccess;
import edu.js.interpreter.preprocessed.instructions.returnsvalue.ReturnsValue;
import edu.js.interpreter.preprocessed.interpretingobjects.FunctionOnStack;
import edu.js.interpreter.tokens.value.OperatorTypes;

public class RangeOfValues implements CaseCondition {
	ReturnsValue lower;

	ReturnsValue higher;

	public RangeOfValues(ReturnsValue lower, ReturnsValue higher) {
		this.lower = lower;
		this.higher = higher;
	}

	public boolean fits(FunctionOnStack f, Object value) {
		ConstantAccess access = new ConstantAccess(value);
		BinaryOperatorEvaluation greater_than_lower = new BinaryOperatorEvaluation(
				access, lower, OperatorTypes.GREATEREQ);
		BinaryOperatorEvaluation less_than_higher = new BinaryOperatorEvaluation(
				access, higher, OperatorTypes.LESSEQ);
		return (Boolean) greater_than_lower.get_value(f)
				&& (Boolean) less_than_higher.get_value(f);
	}
}
