package edu.js.interpreter.preprocessed.instructions.case_statement;

import edu.js.interpreter.preprocessed.instructions.returnsvalue.BinaryOperatorEvaluation;
import edu.js.interpreter.preprocessed.instructions.returnsvalue.ConstantAccess;
import edu.js.interpreter.preprocessed.instructions.returnsvalue.ReturnsValue;
import edu.js.interpreter.preprocessed.interpretingobjects.FunctionOnStack;
import edu.js.interpreter.tokens.value.OperatorTypes;

public class MultipleValues implements CaseCondition {
	ReturnsValue[] values;

	public MultipleValues(ReturnsValue[] values) {
		this.values = values;
	}

	public boolean fits(FunctionOnStack f, Object value) {
		ConstantAccess access = new ConstantAccess(value);
		for (ReturnsValue v : values) {
			if ((Boolean) new BinaryOperatorEvaluation(v, access,
					OperatorTypes.EQUALS).get_value(f)) {
				return true;
			}
		}
		return false;
	}

}
