package edu.js.interpreter.preprocessed.instructions.case_statement;

import edu.js.interpreter.preprocessed.interpretingobjects.FunctionOnStack;

public interface CaseCondition {
	public boolean fits(FunctionOnStack f,Object value);
}
