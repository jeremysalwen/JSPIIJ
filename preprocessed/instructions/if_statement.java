package preprocessed.instructions;

import preprocessed.instructions.returns_value.returns_value;
import preprocessed.interpreting_objects.function_on_stack;

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

	@Override
	public String toString() {
		return "if [" + condition.toString() + "] then [\n" + instruction + ']';
	}
}
