package preprocessed.instructions;

import java.util.LinkedList;
import java.util.List;

import preprocessed.interpreting_objects.function_on_stack;
import processing.run_mode;

public class instruction_grouper implements executable {
	List<executable> instructions;

	public instruction_grouper() {
		instructions = new LinkedList<executable>();
	}

	public void add_command(executable e) {
		instructions.add(e);
	}

	public boolean execute(function_on_stack f) {
		for (executable e : instructions) {
			if (f.program.mode == run_mode.stopped) {
				return true;
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
		for (executable e : instructions) {
			builder.append(e);
		}
		builder.append("end\n");
		return builder.toString();
	}
}
