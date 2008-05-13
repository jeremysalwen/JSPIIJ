package preprocessed;

import java.util.ArrayList;
import java.util.LinkedList;

import pascalTypes.pascal_type;
import pascalTypes.standard_type;

public class function_call extends returns_value {
	String function_name;
	LinkedList<returns_value> arguments;

	public function_call(String function_name,
			LinkedList<returns_value> arguments) {
		this.function_name = function_name;
		this.arguments = arguments;
	}

	@Override
	public pascal_type get_value(function_on_stack f) {
		LinkedList<pascal_type> pascal_args = new LinkedList<pascal_type>();
		for (returns_value v : arguments) {
			pascal_args.add(v.get_value(f));
		}
		return new function_on_stack(f.program, f.program.functions
				.get(function_name)).execute(pascal_args);
	}
}
