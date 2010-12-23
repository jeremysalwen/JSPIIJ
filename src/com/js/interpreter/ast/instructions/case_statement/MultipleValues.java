package com.js.interpreter.ast.instructions.case_statement;

import com.js.interpreter.ast.instructions.returnsvalue.BinaryOperatorEvaluation;
import com.js.interpreter.ast.instructions.returnsvalue.ConstantAccess;
import com.js.interpreter.ast.instructions.returnsvalue.ReturnsValue;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;
import com.js.interpreter.tokens.OperatorTypes;

public class MultipleValues implements CaseCondition {
	ReturnsValue[] values;

	public MultipleValues(ReturnsValue[] values) {
		this.values = values;
	}

	public boolean fits(RuntimeExecutable<?> main,VariableContext f, Object value) throws RuntimePascalException {
		ConstantAccess access = new ConstantAccess(value);
		for (ReturnsValue v : values) {
			if ((Boolean) new BinaryOperatorEvaluation(v, access,
					OperatorTypes.EQUALS).get_value(f, main)) {
				return true;
			}
		}
		return false;
	}

}
