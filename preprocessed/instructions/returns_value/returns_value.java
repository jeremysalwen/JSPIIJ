package preprocessed.instructions.returns_value;

import preprocessed.instructions.executable;
import preprocessed.interpreting_objects.function_on_stack;


public abstract class returns_value implements executable {
	public abstract Object get_value(function_on_stack f);
	
	public void execute(function_on_stack f) {
		this.get_value(f);
	}
}
