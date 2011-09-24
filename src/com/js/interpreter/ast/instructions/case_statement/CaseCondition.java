package com.js.interpreter.ast.instructions.case_statement;

import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public interface CaseCondition {
	public boolean fits(Object value) throws RuntimePascalException;

	public LineInfo getLineNumber();
}
