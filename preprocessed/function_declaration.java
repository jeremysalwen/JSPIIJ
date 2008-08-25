package preprocessed;

import java.util.LinkedList;

import preprocessed.instructions.executable;
import preprocessed.interpreting_objects.function_on_stack;
import processing.pascal_program;

public class function_declaration extends abstract_function{
	public String name;
	public Class return_type;
	public LinkedList<variable_declaration> local_variables;
	public LinkedList<executable> instructions;
	public LinkedList<String> argument_names;
	public LinkedList<Class> argument_types;
	public function_declaration() { /* WARNING, INCOMPLETE CONSTRUCTION */
		local_variables = new LinkedList<variable_declaration>();
		instructions = new LinkedList<executable>();
		argument_names=new LinkedList<String>();
		argument_types=new LinkedList<Class>();
	}

	public void add_local_variable(variable_declaration v) {
		local_variables.add(v);
	}

	public void add_instruction(executable e) {
		instructions.add(e);
	}

	public function_declaration(
			LinkedList<variable_declaration> local_variables,
			LinkedList<executable> instructions) {
		this.local_variables = local_variables;
		this.instructions = instructions;
	}

	@Override
	Class[] get_arg_types() {
		return argument_types.toArray(new Class[argument_types.size()]);
	}

	@Override
	String get_name() {
		return name;
	}

	@Override
	public Object call(pascal_program program, LinkedList arguments) {
		return new function_on_stack(program, this,arguments).execute();
	}

	@Override
	Class get_return_type() {
		return return_type;
	}

}
