package preprocessed.interpreting_objects;

import preprocessed.instructions.executable;


public abstract class returns_value implements executable {
	public abstract Object get_value(function_on_stack f);
	
	public void execute(function_on_stack f) {
		this.get_value(f);
	}
}
