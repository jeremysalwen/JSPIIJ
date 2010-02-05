package edu.js.interpreter.preprocessed.instructions.case_statement;

import edu.js.interpreter.preprocessed.instructions.executable;
import edu.js.interpreter.preprocessed.instructions.returns_value.returns_value;
import edu.js.interpreter.preprocessed.interpreting_objects.function_on_stack;

public class case_instruction implements executable {
	returns_value switch_value;
	case_possibilty[] possibilies;

	public boolean execute(function_on_stack f) {
		Object value = switch_value.get_value(f);
		int index = -1;
		for (int i = 0; i < possibilies.length; i++) {
			if (possibilies[i].condition.fits(f, value)) {
				index = i;
				break;
			}
		}
		while (index < possibilies.length && !possibilies[index++].execute(f))
			;
		return false;
	}

}
