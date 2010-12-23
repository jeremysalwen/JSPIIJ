package com.js.interpreter.ast.instructions.conditional;

import com.js.interpreter.ast.instructions.Executable;
import com.js.interpreter.ast.instructions.ExecutionResult;
import com.js.interpreter.ast.instructions.VariableSet;
import com.js.interpreter.ast.instructions.returnsvalue.BinaryOperatorEvaluation;
import com.js.interpreter.ast.instructions.returnsvalue.ConstantAccess;
import com.js.interpreter.ast.instructions.returnsvalue.ReturnsValue;
import com.js.interpreter.ast.instructions.returnsvalue.VariableAccess;
import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;
import com.js.interpreter.runtime.variables.VariableIdentifier;
import com.js.interpreter.tokens.OperatorTypes;

public class ForStatement implements Executable {
	VariableIdentifier temp_var;

	ReturnsValue first;

	ReturnsValue last;

	Executable command;
	LineInfo line;

	public ForStatement(VariableIdentifier temp_var, ReturnsValue first,
			ReturnsValue last, Executable command, LineInfo line) {
		this.temp_var = temp_var;
		this.first = first;
		this.last = last;
		this.command = command;
		this.line = line;
	}

	public ExecutionResult execute(VariableContext f, RuntimeExecutable<?> main)
			throws RuntimePascalException {
		VariableAccess get_temp_var = new VariableAccess(temp_var, line);
		new VariableSet(temp_var, first, line).execute(f, main);
		BinaryOperatorEvaluation less_than_last = new BinaryOperatorEvaluation(
				get_temp_var, last, OperatorTypes.LESSEQ, this.line);
		VariableSet increment_temp = new VariableSet(temp_var,
				new BinaryOperatorEvaluation(get_temp_var, new ConstantAccess(
						1, this.line), OperatorTypes.PLUS, this.line),
				this.line);

		while_loop: while (((Boolean) less_than_last.get_value(f, null))
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
