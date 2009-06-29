package edu.js.interpreter.preprocessed;

import java.util.ArrayList;
import java.util.List;

import edu.js.interpreter.pascal_types.pascal_type;
import edu.js.interpreter.preprocessed.instructions.executable;
import edu.js.interpreter.preprocessed.instructions.instruction_grouper;
import edu.js.interpreter.preprocessed.interpreting_objects.function_on_stack;
import edu.js.interpreter.processing.pascal_program;

public class function_declaration extends abstract_function {
	public String name;

	public pascal_type return_type;

	public List<variable_declaration> local_variables;

	public executable instructions;

	/* These go together ----> */
	public List<String> argument_names;

	public List<pascal_type> argument_types;

	public List<Boolean> are_varargs;

	/* <----- */

	public function_declaration() { /* WARNING, INCOMPLETE CONSTRUCTION */
		local_variables = new ArrayList<variable_declaration>();
		instructions = new instruction_grouper();
		argument_names = new ArrayList<String>();
		argument_types = new ArrayList<pascal_type>();
		are_varargs = new ArrayList<Boolean>();
	}

	public void add_local_variable(variable_declaration v) {
		local_variables.add(v);
	}

	public function_declaration(List<variable_declaration> local_variables,
			instruction_grouper instructions) {
		this.local_variables = local_variables;
		this.instructions = instructions;
	}

	@Override
	public pascal_type[] get_arg_types() {
		return argument_types.toArray(new pascal_type[argument_types.size()]);
	}

	@Override
	public String get_name() {
		return name;
	}

	@Override
	public Object call(pascal_program program, Object[] arguments) {
		return new function_on_stack(program, this, arguments).execute();
	}

	@Override
	public pascal_type get_return_type() {
		return return_type;
	}

	public pascal_type get_variable_type(String name) {
		int index = argument_names.indexOf(name);
		if (index != -1) {
			return argument_types.get(argument_names.indexOf(name));
		} else {
			for (variable_declaration v : local_variables) {
				if (v.name.equals(name)) {
					return v.type;
				}
			}
		}
		return null;
	}

	@Override
	public boolean is_varargs(int i) {
		return are_varargs.get(i);
	}

	@Override
	public String toString() {
		return super.toString() + ",function";
	}
}
