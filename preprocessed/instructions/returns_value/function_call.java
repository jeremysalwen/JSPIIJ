package preprocessed.instructions.returns_value;

import java.util.LinkedList;

import preprocessed.instructions.executable;
import preprocessed.interpreting_objects.function_on_stack;

public class function_call implements returns_value, executable {
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
		assert (f.program.functions.containsKey(function_name));
		return new function_on_stack(f.program, f.program.functions
				.get(function_name)).execute(pascal_args);
	}

	@Override
	public String toString() {
		return "call function [" + function_name + "] with args [" + arguments
				+ ']';
	}

	@Override
	public void execute(function_on_stack f) {
		get_value(f);
	}
}
