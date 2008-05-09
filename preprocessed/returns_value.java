package preprocessed;

import pascalTypes.pascalType;

public abstract class returns_value<T> implements executable {
	public abstract pascalType<T> get_value(function_on_stack f);
	
	public void execute(function_on_stack f) {
		this.get_value(f);
	}
}
