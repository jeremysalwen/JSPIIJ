package com.js.interpreter.ast;

import com.js.interpreter.pascaltypes.DeclaredType;

public interface ExpressionBuildingContext extends ExpressionContext {
	public void addFunctionDeclaration(FunctionDeclaration f);

	public void addTypedef(String name, DeclaredType type);

	public void addConstDef(ConstantDefinition d);

	public void addVarDef(VariableDeclaration v);
}
