package com.js.interpreter.ast.instructions.case_statement;

import com.js.interpreter.ast.instructions.returnsvalue.BinaryOperatorEvaluation;
import com.js.interpreter.ast.instructions.returnsvalue.ConstantAccess;
import com.js.interpreter.ast.instructions.returnsvalue.ReturnsValue;
import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;
import com.js.interpreter.tokens.OperatorTypes;

public class MultipleValues implements CaseCondition {
	ReturnsValue[] values;
	final LineInfo line;

	public MultipleValues(ReturnsValue[] values, LineInfo line) {
		this.values = values;
		this.line=line;
	}

	@Override
	public boolean fits(RuntimeExecutable<?> main, VariableContext f,
			Object value) throws RuntimePascalException {
		ConstantAccess access = new ConstantAccess(value,line);
		for (ReturnsValue v : values) {
			if ((Boolean) new BinaryOperatorEvaluation(v, access,
					OperatorTypes.EQUALS,line).getValueImpl(f, main)) {
				return true;
			}
		}
		return false;
	}

}
