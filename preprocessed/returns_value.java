package preprocessed;

import pascalTypes.standard_type;

public abstract class returns_value<T> implements executable {
	public abstract standard_type<T> get_value(function_on_stack f);
	
	public void execute(function_on_stack f) {
		this.get_value(f);
	}
}
