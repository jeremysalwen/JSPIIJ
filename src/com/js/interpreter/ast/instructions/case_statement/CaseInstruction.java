package com.js.interpreter.ast.instructions.case_statement;

import com.js.interpreter.ast.instructions.DebuggableExecutable;
import com.js.interpreter.ast.instructions.Executable;
import com.js.interpreter.ast.instructions.ExecutionResult;
import com.js.interpreter.ast.instructions.returnsvalue.ReturnsValue;
import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public class CaseInstruction extends DebuggableExecutable {
	ReturnsValue switch_value;
	CasePossibility[] possibilies;
	LineInfo line;

	public CaseInstruction(LineInfo line) {
		this.line = line;
	}

	@Override
	public ExecutionResult executeImpl(VariableContext f, RuntimeExecutable<?> main)
			throws RuntimePascalException {
		Object value = switch_value.getValue(f, main);
		int index = -1;
		for (int i = 0; i < possibilies.length; i++) {
			if (possibilies[i].condition.fits(main, f, value)) {
				index = i;
				break;
			}
		}
		while_loop: while (index < possibilies.length) {
			switch (possibilies[index++].executeImpl(f, main)) {
			case BREAK:
				break while_loop;
			case RETURN:
				return ExecutionResult.RETURN;
			}
		}
		return ExecutionResult.NONE;
	}

	@Override
	public LineInfo getLineNumber() {
		return line;
	}
}
