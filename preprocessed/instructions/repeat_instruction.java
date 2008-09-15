package preprocessed.instructions;

import preprocessed.instructions.returns_value.returns_value;
import preprocessed.interpreting_objects.function_on_stack;

public class repeat_instruction implements executable {
	executable command;

	returns_value condition;

	public repeat_instruction(executable command, returns_value condition) {
		this.command = command;
		this.condition = condition;
	}

	public boolean execute(function_on_stack f) {
		do {
			if (command.execute(f)) {
				break;
			}
		} while (((Boolean) condition.get_value(f)));
		return false;
	}

}
