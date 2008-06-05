package preprocessed.instructions.returns_value;

import java.util.LinkedList;

import pascal_types.pascal_type;
import preprocessed.function_declaration;
import preprocessed.function_header;
import preprocessed.instructions.executable;
import preprocessed.interpreting_objects.function_on_stack;

public class function_call implements returns_value, executable {
	function_header header;
	LinkedList<returns_value> arguments;

	public function_call(function_header function_name,
			LinkedList<returns_value> arguments) {
		this.header = function_name;
		this.arguments = arguments;
	}

	@Override
	public Object get_value(function_on_stack f) {
		LinkedList<Object> pascal_args = new LinkedList<Object>();
		for (returns_value v : arguments) {
			pascal_args.add(v.get_value(f));
		}
		if (!(f.program.functions.containsKey(header))
				|| f.program.functions.get(header) == null) {
			System.err.println("Could not find function name " + header + "!");
		}
		return new function_on_stack(f.program, f.program.functions.get(header))
				.execute(pascal_args);
	}

	@Override
	public String toString() {
		return "call function [" + header + "] with args [" + arguments + ']';
	}

	@Override
	public void execute(function_on_stack f) {
		get_value(f);
	}

	@Override
	public pascal_type get_type(function_declaration f) {
		return header.return_type;
	}
}
