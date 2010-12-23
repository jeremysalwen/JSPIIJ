package com.js.interpreter.ast.instructions.conditional;

import com.js.interpreter.ast.instructions.Executable;
import com.js.interpreter.ast.instructions.ExecutionResult;
import com.js.interpreter.ast.instructions.returnsvalue.ReturnsValue;
import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public class RepeatInstruction implements Executable {
	Executable command;

	ReturnsValue condition;
	LineInfo line;

	public RepeatInstruction(Executable command, ReturnsValue condition,
			LineInfo line) {
		this.command = command;
		this.condition = condition;
		this.line = line;
	}

	@Override
	public LineInfo getLineNumber() {
		return line;
	}

	@Override
	public ExecutionResult execute(VariableContext f, RuntimeExecutable<?> main)
			throws RuntimePascalException {
		do_loop: do {
			switch (command.execute(f, main)) {
			case BREAK:
				break do_loop;
			case RETURN:
				return ExecutionResult.RETURN;
			}
		} while (!((Boolean) condition.get_value(f, main)));
		return ExecutionResult.NONE;
	}
}
