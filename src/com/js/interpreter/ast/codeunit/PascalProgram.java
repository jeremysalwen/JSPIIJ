package com.js.interpreter.ast.codeunit;

import java.io.Reader;
import java.util.List;

import com.google.common.collect.ListMultimap;
import com.js.interpreter.ast.AbstractFunction;
import com.js.interpreter.ast.FunctionDeclaration;
import com.js.interpreter.ast.instructions.Executable;
import com.js.interpreter.exceptions.ExpectedTokenException;
import com.js.interpreter.exceptions.MisplacedDeclarationException;
import com.js.interpreter.exceptions.ParsingException;
import com.js.interpreter.pascaltypes.RuntimeType;
import com.js.interpreter.runtime.FunctionOnStack;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.codeunit.RuntimePascalProgram;
import com.js.interpreter.startup.ScriptSource;
import com.js.interpreter.tokens.basic.PeriodToken;
import com.js.interpreter.tokens.grouping.BaseGrouperToken;
import com.js.interpreter.tokens.grouping.GrouperToken;

public class PascalProgram extends ExecutableCodeUnit {
	public Executable main;

	public FunctionOnStack main_running;

	public PascalProgram(ListMultimap<String, AbstractFunction> functionTable) {
		super(functionTable);
	}

	public PascalProgram(Reader program,
			ListMultimap<String, AbstractFunction> functionTable,
			String sourcename, List<ScriptSource> includeDirectories)
			throws ParsingException {
		super(program, functionTable, sourcename, includeDirectories);
	}

	@Override
	protected void prepareForParsing() {
	}

	@Override
	public void handleBeginEnd(GrouperToken i) throws ParsingException {
		if (main != null) {
			throw new ParsingException(i.peek().lineInfo,
					"Multiple definitions of main.");
		}
		main = i.get_next_command(this.declarations);
		if (!(i.peek() instanceof PeriodToken)) {
			throw new ExpectedTokenException(".", i.peek());
		}
		i.take();
	}

	@Override
	public RuntimeExecutable<PascalProgram> run() {
		return new RuntimePascalProgram(this);
	}

}
