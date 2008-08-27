package preprocessed.instructions;

import java.util.LinkedList;
import java.util.List;

import preprocessed.interpreting_objects.function_on_stack;

public class instruction_grouper implements executable {
	List<executable> instructions;

	public instruction_grouper() {
		instructions = new LinkedList<executable>();
	}

	public void add_command(executable e) {
		instructions.add(e);
	}

	public void execute(function_on_stack f) {
		for (executable e : instructions) {
			e.execute(f);
		}
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
