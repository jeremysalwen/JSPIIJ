package com.js.interpreter.ast;

import com.js.interpreter.pascaltypes.DeclaredType;

public interface CompileTimeContext {
	public ConstantDefinition getConstantDefinition(String ident);

	public DeclaredType getTypedefType(String ident);
}
