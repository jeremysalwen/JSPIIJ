package com.js.interpreter.ast.instructions;

import com.js.interpreter.ast.expressioncontext.CompileTimeContext;
import com.js.interpreter.exceptions.ParsingException;
import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

import java.util.LinkedList;
import java.util.List;

public class InstructionGrouper extends DebuggableExecutable {
    List<Executable> instructions;
    LineInfo line;

    public InstructionGrouper(LineInfo line) {
        this.line = line;
        instructions = new LinkedList<Executable>();
    }

    @Override
    public LineInfo getLineNumber() {
        return line;
    }

    public void add_command(Executable e) {
        instructions.add(e);
    }

    @Override
    public ExecutionResult executeImpl(VariableContext f,
                                       RuntimeExecutable<?> main) throws RuntimePascalException {
        for (Executable e : instructions) {
            switch (e.execute(f, main)) {
                case BREAK:
                    return ExecutionResult.BREAK;
                case RETURN:
                    return ExecutionResult.RETURN;
            }
        }
        return ExecutionResult.NONE;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("begin\n");
        for (Executable e : instructions) {
            builder.append(e);
        }
        builder.append("end\n");
        return builder.toString();
    }

    @Override
    public Executable compileTimeConstantTransform(CompileTimeContext c) throws ParsingException {
        InstructionGrouper nig = new InstructionGrouper(line);
        for (Executable e : instructions) {
            Executable transformed = e.compileTimeConstantTransform(c);
            if (transformed == null) {
                nig.instructions.add(e);
            } else if (transformed instanceof NopInstruction) {
                continue;
            } else {
                nig.instructions.add(transformed);
            }
        }
        if (nig.instructions.size() == 0) {
            return new NopInstruction(line);
        } else {
            return nig;
        }
    }
}
