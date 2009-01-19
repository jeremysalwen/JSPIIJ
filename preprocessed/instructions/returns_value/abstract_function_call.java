package preprocessed.instructions.returns_value;

import preprocessed.abstract_function;
import preprocessed.dummy_declaration;
import preprocessed.function_declaration;
import preprocessed.instructions.executable;
import preprocessed.instructions.variable_set;
import preprocessed.interpreting_objects.function_on_stack;
import preprocessed.interpreting_objects.pointer;
import preprocessed.interpreting_objects.variables.variable_identifier;
import processing.pascal_program;

public class abstract_function_call implements returns_value, executable {
	String name;

	returns_value[] arguments;

	public abstract_function_call(String name, returns_value[] arguments) {
		this.name = name;
		this.arguments = arguments;
	}

	public Object get_value(function_on_stack f) {
		Class[] arg_types = new Class[arguments.length];
		Object[] values = new Object[arguments.length];
		for (int i = 0; i < arguments.length; i++) {
			arg_types[i] = arguments[i].get_type(f.program, f.prototype);

		}
		dummy_declaration header = new dummy_declaration(name, arg_types);
		abstract_function called_function = f.program.callable_functions
				.get(header);
		for (int i = 0; i < values.length; i++) {
			if (called_function.is_varargs(i)) {
				if (!(arguments[i] instanceof variable_access)) {
					System.err
							.println("Attempted to pass non-variable as variable argument");
					System.exit(0);
				}
				variable_identifier a = (variable_identifier) arguments[i];
				values[i] = new pointer(a.get(a.size() - 1), f
						.get_variable_holder(a));
			} else {
				values[i] = arguments[i].get_value(f);
			}
		}
		Object result = called_function.call(f.program, values);
		return result;
	}

	public String toString() {
		return "call function [" + name + "] with args [" + arguments + ']';
	}

	public boolean execute(function_on_stack f) {
		get_value(f);
		return false;
	}

	public Class get_type(pascal_program p, function_declaration f) {
		Class[] arg_types = new Class[arguments.length];
		for (int i = 0; i < arguments.length; i++) {
			arg_types[i] = arguments[i].get_type(p, f);
		}
		dummy_declaration header = new dummy_declaration(name, arg_types);
		return p.callable_functions.get(header).get_return_type();
	}
}
