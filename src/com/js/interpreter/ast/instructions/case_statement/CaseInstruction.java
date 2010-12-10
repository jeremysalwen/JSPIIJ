package com.js.interpreter.ast.instructions.case_statement;

import com.js.interpreter.ast.instructions.Executable;
import com.js.interpreter.ast.instructions.ExecutionResult;
import com.js.interpreter.ast.instructions.returnsvalue.ReturnsValue;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;

public class CaseInstruction implements Executable {
	ReturnsValue switch_value;
	CasePossibility[] possibilies;

	public ExecutionResult execute(VariableContext f, RuntimeExecutable<?> main) {
		Object value = switch_value.get_value(f, main);
		int index = -1;
		for (int i = 0; i < possibilies.length; i++) {
			if (possibilies[i].condition.fits(main, f, value)) {
				index = i;
				break;
			}
		}
		while_loop: while (index < possibilies.length) {
			switch (possibilies[index++].execute(f, main)) {
			case BREAK:
				break while_loop;
			case RETURN:
				return ExecutionResult.RETURN;
			}
		}
		return ExecutionResult.NONE;
	}
}
