package edu.js.interpreter.preprocessed.instructions;

import edu.js.interpreter.preprocessed.instructions.returnsvalue.ReturnsValue;
import edu.js.interpreter.preprocessed.interpretingobjects.FunctionOnStack;

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

	public boolean execute(FunctionOnStack f) {
		if (((Boolean) (condition.get_value(f))).booleanValue()) {
			return instruction.execute(f);
		} else {
			if (else_instruction != null) {
				return else_instruction.execute(f);
			}
			return false;
		}
	}

	@Override
	public String toString() {
		return "if [" + condition.toString() + "] then [\n" + instruction + ']';
	}
}
