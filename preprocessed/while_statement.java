package preprocessed;

import pascalTypes.standard_type;

public class while_statement implements executable {
	returns_value condition;
	executable command;

	public while_statement(returns_value condition, executable command) {
		this.condition = condition;
		this.command = command;
	}

	@Override
	public void execute(function_on_stack f) {
		while (((standard_type<Boolean>) condition.get_value(f)).get()) {
			command.execute(f);
		}
	}

}
