package com.js.interpreter.ast.codeunit;

import java.io.Reader;
import java.util.List;

import com.google.common.collect.ListMultimap;
import com.js.interpreter.ast.AbstractFunction;
import com.js.interpreter.ast.FunctionDeclaration;
import com.js.interpreter.ast.VariableDeclaration;
import com.js.interpreter.classgeneration.CustomTypeGenerator;
import com.js.interpreter.exceptions.ExpectedTokenException;
import com.js.interpreter.exceptions.ParsingException;
import com.js.interpreter.pascaltypes.DeclaredType;
import com.js.interpreter.pascaltypes.RuntimeType;
import com.js.interpreter.runtime.FunctionOnStack;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.codeunit.RuntimePascalProgram;
import com.js.interpreter.startup.ScriptSource;
import com.js.interpreter.tokens.basic.PeriodToken;
import com.js.interpreter.tokens.grouping.BaseGrouperToken;

public class PascalProgram extends ExecutableCodeUnit {
	public FunctionDeclaration main;

	public FunctionOnStack main_running;

	public PascalProgram(ListMultimap<String, AbstractFunction> functionTable,
			CustomTypeGenerator type_generator) {
		super(functionTable, type_generator);
	}

	public PascalProgram(Reader program,
			ListMultimap<String, AbstractFunction> functionTable,
			String sourcename, List<ScriptSource> includeDirectories,
			CustomTypeGenerator type_generator) throws ParsingException {
		super(program, functionTable, sourcename, includeDirectories,
				type_generator);
	}

	@Override
	protected void prepareForParsing() {
		main = new FunctionDeclaration(this);
		main.are_varargs = new boolean[0];
		main.argument_names = new String[0];
		main.argument_types = new RuntimeType[0];
		main.name = "main";
	}

	@Override
	protected void handleBeginEnd(BaseGrouperToken i) throws ParsingException {
		if (main.instructions != null) {
			throw new ParsingException(i.peek().lineInfo,
					"Multiple definitions of main.");
		}
		main.instructions = main.get_next_command(i);
		if (!(i.peek() instanceof PeriodToken)) {
			throw new ExpectedTokenException(".", i.peek());
		}
		i.take();
	}

	@Override
	public RuntimeExecutable<PascalProgram> run() {
		return new RuntimePascalProgram(this);
	}

	@Override
	protected void handleGloablVarDeclaration(
			List<VariableDeclaration> declarations) {
		main.local_variables.addAll(declarations);
	}

	@Override
	public DeclaredType getGlobalVarType(String name) {
		return main.get_variable_type(name);
	}
}
