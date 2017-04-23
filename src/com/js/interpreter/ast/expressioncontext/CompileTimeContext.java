package com.js.interpreter.ast.expressioncontext;

import com.js.interpreter.ast.ConstantDefinition;
import com.js.interpreter.pascaltypes.DeclaredType;

public interface CompileTimeContext {
    public ConstantDefinition getConstantDefinition(String ident);

    public DeclaredType getTypedefType(String ident);
}
