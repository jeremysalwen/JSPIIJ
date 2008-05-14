package preprocessed;

import java.util.LinkedList;

public class function_call extends returns_value {
	String function_name;
	LinkedList<returns_value> arguments;

	public function_call(String function_name,
			LinkedList<returns_value> arguments) {
		this.function_name = function_name;
		this.arguments = arguments;
	}

	@Override
	public Object get_value(function_on_stack f) {
		LinkedList<Object> pascal_args = new LinkedList<Object>();
		for (returns_value v : arguments) {
			pascal_args.add(v.get_value(f));
		}
		return new function_on_stack(f.program, f.program.functions
				.get(function_name)).execute(pascal_args);
	}
}
