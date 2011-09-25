package com.js.interpreter.ast;

import java.util.List;
import java.util.ListIterator;

import com.js.interpreter.ast.codeunit.CodeUnit;
import com.js.interpreter.ast.instructions.Executable;
import com.js.interpreter.ast.returnsvalue.ReturnsValue;
import com.js.interpreter.exceptions.ParsingException;
import com.js.interpreter.exceptions.SameNameException;
import com.js.interpreter.tokens.Token;
import com.js.interpreter.tokens.WordToken;
import com.js.interpreter.tokens.grouping.GrouperToken;

public interface ExpressionContext extends CompileTimeContext {
	public ReturnsValue getIdentifierValue(WordToken name)
			throws ParsingException;

	void verifyNonConflictingSymbol(NamedEntity n) throws SameNameException;

	public VariableDeclaration getVariableDefinition(String ident);

	public void getCallableFunctions(String name,
			List<List<AbstractFunction>> listsofar);

	public boolean functionExists(String name);

	public CodeUnit root();

	public abstract Executable handleUnrecognizedStatement(Token next,
			GrouperToken container) throws ParsingException;

	public abstract boolean handleUnrecognizedDeclaration(Token next,
			GrouperToken container) throws ParsingException;

}
