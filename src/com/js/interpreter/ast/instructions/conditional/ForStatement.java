package com.js.interpreter.ast.instructions.conditional;

import com.js.interpreter.ast.expressioncontext.CompileTimeContext;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.instructions.*;
import com.js.interpreter.ast.returnsvalue.ConstantAccess;
import com.js.interpreter.ast.returnsvalue.LValue;
import com.js.interpreter.ast.returnsvalue.RValue;
import com.js.interpreter.ast.returnsvalue.operators.BinaryOperatorEvaluation;
import com.js.interpreter.exceptions.ParsingException;
import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;
import com.js.interpreter.tokens.OperatorTypes;

public class ForStatement extends DebuggableExecutable {
    SetValueExecutable setfirst;
    RValue lessthanlast;
    SetValueExecutable increment_temp;
    Executable command;
    LineInfo line;

    public ForStatement(ExpressionContext f, LValue temp_var,
                        RValue first, RValue last, Executable command,
                        LineInfo line) throws ParsingException {
        this.line = line;
        setfirst = new Assignment(temp_var,first,line);
        lessthanlast = BinaryOperatorEvaluation.generateOp(f, temp_var, last,
                OperatorTypes.LESSEQ, this.line);
        increment_temp = new Assignment(temp_var, BinaryOperatorEvaluation.generateOp(
                f, temp_var, new ConstantAccess(1, this.line),
                OperatorTypes.PLUS, this.line), line);

        this.command = command;

    }

    @Override
    public ExecutionResult executeImpl(VariableContext f,
                                       RuntimeExecutable<?> main) throws RuntimePascalException {
        setfirst.execute(f, main);
        while_loop:
        while ((Boolean) lessthanlast.getValue(f, main)) {
            switch (command.execute(f, main)) {
                case RETURN:
                    return ExecutionResult.RETURN;
                case BREAK:
                    break while_loop;
            }
            increment_temp.execute(f, main);
        }
        return ExecutionResult.NONE;
    }

    @Override
    public LineInfo getLineNumber() {
        return line;
    }

    @Override
    public Executable compileTimeConstantTransform(CompileTimeContext c)
            throws ParsingException {
        SetValueExecutable first = setfirst.compileTimeConstantTransform(c);
        SetValueExecutable inc = increment_temp.compileTimeConstantTransform(c);
        Executable comm = command.compileTimeConstantTransform(c);
        RValue comp = lessthanlast;
        Object val = lessthanlast.compileTimeValue(c);
        if (val != null) {
            if ((Boolean) val) {
                return first;
            } else {
                comp = new ConstantAccess(val, lessthanlast.getLineNumber());
            }
        }
        return new DowntoForStatement(first, comp, inc, comm, line);
    }
}
