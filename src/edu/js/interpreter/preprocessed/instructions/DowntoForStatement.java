package edu.js.interpreter.preprocessed.instructions;

import edu.js.interpreter.preprocessed.instructions.returnsvalue.BinaryOperatorEvaluation;
import edu.js.interpreter.preprocessed.instructions.returnsvalue.ConstantAccess;
import edu.js.interpreter.preprocessed.instructions.returnsvalue.ReturnsValue;
import edu.js.interpreter.preprocessed.instructions.returnsvalue.VariableAccess;
import edu.js.interpreter.preprocessed.interpretingobjects.FunctionOnStack;
import edu.js.interpreter.preprocessed.interpretingobjects.variables.VariableIdentifier;
import edu.js.interpreter.tokens.value.OperatorTypes;

public class DowntoForStatement implements Executable {
	VariableIdentifier temp_var;

	ReturnsValue first;

	ReturnsValue last;

	Executable command;

	public DowntoForStatement(VariableIdentifier temp_var,
			ReturnsValue first, ReturnsValue last, Executable command) {
		this.temp_var = temp_var;
		this.first = first;
		this.last = last;
		this.command = command;
	}

	public boolean execute(FunctionOnStack f) {
		ConstantAccess last_value = new ConstantAccess(last.get_value(f));
		VariableAccess get_temp_var = new VariableAccess(temp_var);
		new VariableSet(temp_var, first).execute(f);
		BinaryOperatorEvaluation less_than_last = new BinaryOperatorEvaluation(
				get_temp_var, last_value, OperatorTypes.GREATEREQ);
		VariableSet increment_temp = new VariableSet(temp_var,
				new BinaryOperatorEvaluation(get_temp_var,
						new ConstantAccess(1), OperatorTypes.MINUS));

		while (((Boolean) less_than_last.get_value(f)).booleanValue()) {
			if(command.execute(f)) {
				break;
			}
			increment_temp.execute(f);
		}
		return false;
	}

}
