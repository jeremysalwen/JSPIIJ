package preprocessed;

import pascalTypes.pascal_type;

public abstract class returns_value implements executable {
	public abstract pascal_type get_value(function_on_stack f);
	
	public void execute(function_on_stack f) {
		this.get_value(f);
	}
}
