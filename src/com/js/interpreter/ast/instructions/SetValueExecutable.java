package com.js.interpreter.ast.instructions;

import com.js.interpreter.ast.instructions.returnsvalue.ReturnsValue;

public interface SetValueExecutable extends Executable {
	public void setAssignedValue(ReturnsValue value);
}
