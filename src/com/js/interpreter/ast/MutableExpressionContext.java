package com.js.interpreter.ast;

import java.util.List;

import com.js.interpreter.ast.instructions.Executable;
import com.js.interpreter.exceptions.ParsingException;
import com.js.interpreter.pascaltypes.DeclaredType;
import com.js.interpreter.tokens.Token;
import com.js.interpreter.tokens.grouping.BaseGrouperToken;
import com.js.interpreter.tokens.grouping.GrouperToken;

public interface MutableExpressionContext extends ExpressionContext {
	public void declareTypedef(String name, DeclaredType type);

	public void declareVariable(VariableDeclaration v);

	public void declareFunction(FunctionDeclaration f);

	public void declareConst(ConstantDefinition c);



}
