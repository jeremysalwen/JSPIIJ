package preprocessed;

import java.util.ArrayList;
import java.util.List;

import pascal_types.pascal_type;
import preprocessed.instructions.executable;
import preprocessed.interpreting_objects.function_on_stack;
import processing.pascal_program;

public class function_declaration extends abstract_function {
	public String name;

	public pascal_type return_type;

	public List<variable_declaration> local_variables;

	public List<executable> instructions;

	/* These go together ----> */
	public List<String> argument_names;

	public List<pascal_type> argument_types;

	public List<Boolean> are_varargs;

	/* <----- */

	public function_declaration() { /* WARNING, INCOMPLETE CONSTRUCTION */
		local_variables = new ArrayList<variable_declaration>();
		instructions = new ArrayList<executable>();
		argument_names = new ArrayList<String>();
		argument_types = new ArrayList<pascal_type>();
		are_varargs = new ArrayList<Boolean>();
	}

	public void add_local_variable(variable_declaration v) {
		local_variables.add(v);
	}

	public void add_instruction(executable e) {
		instructions.add(e);
	}

	public function_declaration(List<variable_declaration> local_variables,
			List<executable> instructions) {
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
