package com.js.interpreter.ast.instructions.case_statement;

import com.js.interpreter.ast.instructions.Executable;
import com.js.interpreter.ast.instructions.ExecutionResult;
import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public class CasePossibility implements Executable {
	/**
	 * This class represents a line in a case statement.
	 */
	CaseCondition condition;

	Executable[] commands;
	LineInfo line;

	public CasePossibility(CaseCondition condition, Executable[] commands,
			LineInfo line) {
		this.condition = condition;
		this.commands = commands;
		this.line = line;
	}

	/**
	 * Executes the contained commands in this branch.
	 * 
	 * @param value
	 *            the value being examined in this case statement.
	 * @return Whether or not it has broken.
	 */
	@Override
	public ExecutionResult execute(VariableContext f, RuntimeExecutable<?> main)
			throws RuntimePascalException {
		for_loop: for (Executable e : commands) {
			switch (e.execute(f, main)) {
			case RETURN:
				return ExecutionResult.RETURN;
			case BREAK:
				break for_loop;
			}
		}
		return ExecutionResult.NONE;
	}

	@Override
	public LineInfo getLineNumber() {
		return line;
	}
}
