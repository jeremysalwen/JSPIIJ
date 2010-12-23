package com.js.interpreter.ast.instructions.conditional;

import com.js.interpreter.ast.instructions.Executable;
import com.js.interpreter.ast.instructions.ExecutionResult;
import com.js.interpreter.ast.instructions.returnsvalue.ReturnsValue;
import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public class WhileStatement implements Executable {
	ReturnsValue condition;

	Executable command;
	LineInfo line;

	public WhileStatement(ReturnsValue condition, Executable command,
			LineInfo line) {
		this.condition = condition;
		this.command = command;
		this.line = line;
	}

	public ExecutionResult execute(VariableContext f, RuntimeExecutable<?> main)
			throws RuntimePascalException {
		while_loop: while ((Boolean) condition.get_value(f, main)) {
			switch (command.execute(f, main)) {
			case BREAK:
				break while_loop;
			case RETURN:
				return ExecutionResult.RETURN;
			}
		}
		return ExecutionResult.NONE;
	}

	@Override
	public String toString() {
		return "while [" + condition + "] do [" + command + ']';
	}

	@Override
	public LineInfo getLineNumber() {
		return line;
	}
}
