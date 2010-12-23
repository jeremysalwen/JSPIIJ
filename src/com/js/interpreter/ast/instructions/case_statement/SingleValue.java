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
	ReturnsValue value;
	LineInfo line;

	public SingleValue(ReturnsValue value, LineInfo line) {
		this.value = value;
		this.line = line;
	}

	@Override
	public boolean fits(RuntimeExecutable<?> main, VariableContext f,
			Object value) throws RuntimePascalException {
		return (Boolean) new BinaryOperatorEvaluation(new ConstantAccess(value,
				line), this.value, OperatorTypes.EQUALS, line).get_value(f,
				main);
	}

}
