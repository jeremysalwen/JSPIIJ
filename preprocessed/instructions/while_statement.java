package preprocessed.instructions;

import preprocessed.interpreting_objects.function_on_stack;
import preprocessed.interpreting_objects.returns_value;

public class while_statement implements executable {
	returns_value condition;
	executable command;

	public while_statement(returns_value condition, executable command) {
		this.condition = condition;
		this.command = command;
	}

	@Override
	public void execute(function_on_stack f) {
		while ((Boolean) condition.get_value(f)) {
			command.execute(f);
		}
	}

}
