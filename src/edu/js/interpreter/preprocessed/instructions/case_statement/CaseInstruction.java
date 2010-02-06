package edu.js.interpreter.preprocessed.instructions.case_statement;

import edu.js.interpreter.preprocessed.instructions.Executable;
import edu.js.interpreter.preprocessed.instructions.returnsvalue.ReturnsValue;
import edu.js.interpreter.preprocessed.interpretingobjects.FunctionOnStack;

public class CaseInstruction implements Executable {
	ReturnsValue switch_value;
	CasePossibility[] possibilies;

	public boolean execute(FunctionOnStack f) {
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
