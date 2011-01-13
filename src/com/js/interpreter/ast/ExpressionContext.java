package com.js.interpreter.ast;

import java.util.List;

import com.js.interpreter.ast.codeunit.CodeUnit;

public interface ExpressionContext extends CompileTimeContext {

	public VariableDeclaration getVariableDefinition(String ident);

	public List<AbstractFunction> getCallableFunctions(String name);

	public boolean functionExists(String name);

	public CodeUnit root();
}
