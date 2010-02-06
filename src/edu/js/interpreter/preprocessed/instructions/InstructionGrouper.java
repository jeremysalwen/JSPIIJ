package edu.js.interpreter.preprocessed.instructions;

import java.util.LinkedList;
import java.util.List;

import edu.js.interpreter.preprocessed.interpretingobjects.FunctionOnStack;
import edu.js.interpreter.processing.RunMode;

public class InstructionGrouper implements Executable {
	List<Executable> instructions;

	public InstructionGrouper() {
		instructions = new LinkedList<Executable>();
	}

	public void add_command(Executable e) {
		instructions.add(e);
	}

	public boolean execute(FunctionOnStack f) {
		for (Executable e : instructions) {
			switch (f.program.mode) {
			case stopped:
				return true;
			case paused:
				while (f.program.mode == RunMode.paused) {
					try {
						f.program.wait();
					} catch (InterruptedException e1) {
					}
				}
			}
			if (e.execute(f)) {
				return true;
			}
		}
		return false;
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
