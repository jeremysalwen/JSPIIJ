package com.js.interpreter.ast.instructions.case_statement;

import com.js.interpreter.ast.instructions.returnsvalue.BinaryOperatorEvaluation;
import com.js.interpreter.ast.instructions.returnsvalue.ConstantAccess;
import com.js.interpreter.ast.instructions.returnsvalue.ReturnsValue;
import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;
import com.js.interpreter.tokens.OperatorTypes;

public class SingleValue implements CaseCondition {
	ReturnsValue values;

	public SingleValue(ReturnsValue values) {
		this.values = values;
	}

	@Override
	public boolean fits(RuntimeExecutable<?> main, VariableContext f,
			Object value) throws RuntimePascalException {
		ConstantAccess access = new ConstantAccess(value,
				values.getLineNumber());
		if ((Boolean) new BinaryOperatorEvaluation(values, access,
				OperatorTypes.EQUALS, values.getLineNumber()).getValueImpl(f,
				main)) {
			return true;
		}
		return false;
	}

	@Override
	public LineInfo getLineNumber() {
		return values.getLineNumber();
	}

}
