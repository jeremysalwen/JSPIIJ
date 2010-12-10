package com.js.interpreter.ast.instructions.conditional;

import com.js.interpreter.ast.instructions.Executable;
import com.js.interpreter.ast.instructions.ExecutionResult;
import com.js.interpreter.ast.instructions.returnsvalue.ReturnsValue;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;

public class WhileStatement implements Executable {
	ReturnsValue condition;

	Executable command;

	public WhileStatement(ReturnsValue condition, Executable command) {
		this.condition = condition;
		this.command = command;
	}

	public ExecutionResult execute(VariableContext f,RuntimeExecutable<?> main) {
		while_loop: while ((Boolean) condition.get_value(f, main)) {
			switch (command.execute(f,main)) {
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
}
