package preprocessed;

import java.util.LinkedList;

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
}
