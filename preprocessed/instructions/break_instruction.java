package preprocessed.instructions;

import preprocessed.interpreting_objects.function_on_stack;

public class break_instruction implements executable {

	public void execute(function_on_stack f) {
		System.err.println("Break instructions should never be executed");
		System.exit(1);
	}

}
