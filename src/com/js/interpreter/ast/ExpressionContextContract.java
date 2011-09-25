package com.js.interpreter.ast;

import java.util.List;

import com.js.interpreter.ast.instructions.Executable;
import com.js.interpreter.exceptions.ParsingException;
import com.js.interpreter.tokens.Token;
import com.js.interpreter.tokens.grouping.BaseGrouperToken;
import com.js.interpreter.tokens.grouping.BeginEndToken;
import com.js.interpreter.tokens.grouping.GrouperToken;

public interface ExpressionContextContract {
	public abstract Executable handleUnrecognizedStatement(Token next,
			GrouperToken container) throws ParsingException;

	public abstract boolean handleUnrecognizedDeclaration(Token next,
			GrouperToken container) throws ParsingException;

	public void handleBeginEnd(GrouperToken i) throws ParsingException;

}
