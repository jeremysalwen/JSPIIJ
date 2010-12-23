package com.js.interpreter.ast.instructions.conditional;

import com.js.interpreter.ast.instructions.Executable;
import com.js.interpreter.ast.instructions.ExecutionResult;
import com.js.interpreter.ast.instructions.returnsvalue.ReturnsValue;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public class IfStatement implements Executable {
	ReturnsValue condition;

	Executable instruction;

	Executable else_instruction;

	public IfStatement(ReturnsValue condition, Executable instruction,
			Executable else_instruction) {
		this(condition, instruction);
		this.else_instruction = else_instruction;
	}

	public IfStatement(ReturnsValue condition, Executable instruction) {
		this.condition = condition;
		this.instruction = instruction;
	}

	public ExecutionResult execute(VariableContext f, RuntimeExecutable<?> main) throws RuntimePascalException {
		if (((Boolean) (condition.get_value(f, null))).booleanValue()) {
			return instruction.execute(f, main);
		} else {
			if (else_instruction != null) {
				return else_instruction.execute(f, main);
			}
			return ExecutionResult.NONE;
		}
	}

	@Override
	public String toString() {
		return "if [" + condition.toString() + "] then [\n" + instruction + ']';
	}
}
