package preprocessed;

import java.util.ArrayList;
import java.util.LinkedList;

import pascalTypes.pascalType;

public class function_call extends returns_value {
	String function_name;
	LinkedList<returns_value> arguments;

	public function_call(String function_name,
			LinkedList<returns_value> arguments) {
		this.function_name = function_name;
		this.arguments = arguments;
	}

	@Override
	public pascalType get_value(function_on_stack f) {
		ArrayList<pascalType> pascal_args = new ArrayList<pascalType>(arguments
				.size());
		for (returns_value v : arguments) {
			pascal_args.add(v.get_value(f));
		}
		return new function_on_stack(f.program, f.program.functions
				.get(function_name)).execute(pascal_args);
	}
}
