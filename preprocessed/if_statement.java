package preprocessed;


public class if_statement implements executable {
	returns_value<Boolean> condition;
	executable instruction;
	public if_statement(returns_value<Boolean> condition, executable instruction) {
		this.condition=condition;
		this.instruction=instruction;
	}
	
	public void execute(function_on_stack f) {
		if((condition.get_value(f)).get()) {
			instruction.execute(f);
		}
	}

}
