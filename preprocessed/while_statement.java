package preprocessed;

public class while_statement implements executable {
	returns_value<Boolean> condition;
	executable command;
	public while_statement(returns_value<Boolean> condition, executable command) {
		this.condition=condition;
		this.command=command;
	}
	@Override
	public void execute(function_on_stack f) {
		while(condition.get_value(f).get()) {
			command.execute(f);
		}
	}

}
