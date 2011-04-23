package com.js.interpreter.ast.instructions.conditional;

import com.js.interpreter.ast.instructions.DebuggableExecutable;
import com.js.interpreter.ast.instructions.Executable;
import com.js.interpreter.ast.instructions.ExecutionResult;
import com.js.interpreter.ast.instructions.SetValueExecutable;
import com.js.interpreter.ast.instructions.returnsvalue.BinaryOperatorEvaluation;
import com.js.interpreter.ast.instructions.returnsvalue.ConstantAccess;
import com.js.interpreter.ast.instructions.returnsvalue.ReturnsValue;
import com.js.interpreter.exceptions.UnassignableTypeException;
import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;
import com.js.interpreter.tokens.OperatorTypes;

public class DowntoForStatement extends DebuggableExecutable {
	SetValueExecutable setfirst;
	ReturnsValue lessthanlast;
	SetValueExecutable increment_temp;
	Executable command;
	LineInfo line;

	public DowntoForStatement(ReturnsValue temp_var, ReturnsValue first,
			ReturnsValue last, Executable command, LineInfo line)
			throws UnassignableTypeException {
		setfirst = temp_var.createSetValueInstruction(first);
		lessthanlast = new BinaryOperatorEvaluation(temp_var, last,
				OperatorTypes.GREATEREQ, this.line);
		increment_temp = temp_var
				.createSetValueInstruction(new BinaryOperatorEvaluation(
						temp_var, new ConstantAccess(1, this.line),
						OperatorTypes.MINUS, this.line));

		this.command = command;
		this.line = line;
	}

	@Override
	public ExecutionResult executeImpl(VariableContext f,
			RuntimeExecutable<?> main) throws RuntimePascalException {
		setfirst.execute(f, main);
		while_loop: while (((Boolean) lessthanlast.getValue(f, main))
				.booleanValue()) {
			switch (command.execute(f, main)) {
			case RETURN:
				return ExecutionResult.RETURN;
			case BREAK:
				break while_loop;
			}
			increment_temp.execute(f, main);
		}
		return ExecutionResult.NONE;
	}

	@Override
	public LineInfo getLineNumber() {
		return line;
	}
}
