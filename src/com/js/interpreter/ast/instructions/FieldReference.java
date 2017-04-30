package com.js.interpreter.ast.instructions;

import com.js.interpreter.ast.expressioncontext.CompileTimeContext;
import com.js.interpreter.ast.returnsvalue.RValue;
import com.js.interpreter.exceptions.ParsingException;
import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.runtime.Reference;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;
import com.js.interpreter.runtime.variables.ContainsVariables;

public class FieldReference implements Reference {
    ContainsVariables container;
    String name;

    public FieldReference(ContainsVariables container, String name) {
        this.container = container;
        this.name = name;
    }

    @Override
    public void set(Object value) {
        container.set_var(name, value);
    }

    @Override
    public Object get() throws RuntimePascalException {
        return container.get_var(name);
    }

    @Override
    public Reference clone() {
        return null;
    }
}
