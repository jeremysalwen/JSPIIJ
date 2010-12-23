package com.js.interpreter.ast.instructions;

import java.util.LinkedList;
import java.util.List;

import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public class InstructionGrouper implements Executable {
	List<Executable> instructions;
	LineInfo line;

	public InstructionGrouper(LineInfo line) {
		this.line = line;
		instructions = new LinkedList<Executable>();
	}

	@Override
	public LineInfo getLineNumber() {
		return line;
	}

	public void add_command(Executable e) {
		instructions.add(e);
	}

	public ExecutionResult execute(VariableContext f, RuntimeExecutable<?> main)
			throws RuntimePascalException {
		forloop: for (Executable e : instructions) {
			/*
			 * switch (f.parentContext.mode) { case stopped: return
			 * ExecutionResult.RETURN; case paused: while (f.parentContext.mode
			 * == RunMode.paused) { try { f.parentContext.wait(); } catch
			 * (InterruptedException e1) { } } }
			 */
			switch (e.execute(f, main)) {
			case BREAK:
				break forloop;
			case RETURN:
				return ExecutionResult.RETURN;
			}
		}
		return ExecutionResult.NONE;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("begin\n");
		for (Executable e : instructions) {
			builder.append(e);
		}
		builder.append("end\n");
		return builder.toString();
	}
}
