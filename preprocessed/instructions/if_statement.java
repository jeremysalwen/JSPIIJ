package preprocessed.instructions;

import preprocessed.interpreting_objects.function_on_stack;
import preprocessed.interpreting_objects.returns_value;


public class if_statement implements executable {
	returns_value condition;
	executable instruction;

	public if_statement(returns_value condition, executable instruction) {
		this.condition = condition;
		this.instruction = instruction;
	}

	public void execute(function_on_stack f) {
		if (((Boolean) (condition.get_value(f))).booleanValue()) {
			instruction.execute(f);
		}
	}
}
