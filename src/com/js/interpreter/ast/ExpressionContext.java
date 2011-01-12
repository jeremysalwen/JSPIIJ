package com.js.interpreter.ast;

import java.util.List;

import com.js.interpreter.ast.codeunit.CodeUnit;
import com.js.interpreter.pascaltypes.DeclaredType;

public interface ExpressionContext {

	public DeclaredType getVariableType(String ident);

	public Object getConstant(String ident);
	
	public DeclaredType getTypedefType(String ident);

	public List<AbstractFunction> getCallableFunctions(String name);

	public boolean functionExists(String name);

	public CodeUnit root();
}
