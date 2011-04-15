package com.js.interpreter.ast;

import java.util.List;

import com.js.interpreter.ast.codeunit.CodeUnit;
import com.js.interpreter.exceptions.SameNameException;

public interface ExpressionContext extends CompileTimeContext {
	void verifyNonConflictingSymbol(NamedEntity n) throws SameNameException;

	public VariableDeclaration getVariableDefinition(String ident);

	public List<AbstractFunction> getCallableFunctions(String name);

	public boolean functionExists(String name);

	public CodeUnit root();
}
