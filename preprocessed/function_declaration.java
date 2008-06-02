package preprocessed;

import java.util.LinkedList;

import preprocessed.instructions.executable;
import preprocessed.interpreting_objects.function_on_stack;
import processing.pascal_program;

public class function_declaration {
	public function_header header;
	public LinkedList<variable_declaration> local_variables;
	public LinkedList<executable> instructions;

	public function_declaration() { /* WARNING, INCOMPLETE CONSTRUCTION */
		local_variables = new LinkedList<variable_declaration>();
		instructions = new LinkedList<executable>();
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

	public function_on_stack instantiate(pascal_program program) {
		return new function_on_stack(program, this);
	}

}
