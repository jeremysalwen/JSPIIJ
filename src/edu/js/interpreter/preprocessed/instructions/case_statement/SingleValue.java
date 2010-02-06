package edu.js.interpreter.preprocessed.instructions.case_statement;

import edu.js.interpreter.preprocessed.instructions.returnsvalue.BinaryOperatorEvaluation;
import edu.js.interpreter.preprocessed.instructions.returnsvalue.ConstantAccess;
import edu.js.interpreter.preprocessed.instructions.returnsvalue.ReturnsValue;
import edu.js.interpreter.preprocessed.interpretingobjects.FunctionOnStack;
import edu.js.interpreter.tokens.value.OperatorTypes;

public class SingleValue implements CaseCondition {
	ReturnsValue value;

	public SingleValue(ReturnsValue value) {
		this.value = value;
	}

	public boolean fits(FunctionOnStack f, Object value) {
		return (Boolean) new BinaryOperatorEvaluation(new ConstantAccess(
				value), this.value, OperatorTypes.EQUALS).get_value(f);
	}

}
