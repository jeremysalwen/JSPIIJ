package com.js.interpreter.ast.codeunit;

import java.io.Reader;
import java.util.List;

import com.google.common.collect.ListMultimap;
import com.js.interpreter.ast.AbstractFunction;
import com.js.interpreter.classgeneration.CustomTypeGenerator;
import com.js.interpreter.exceptions.ParsingException;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.startup.ScriptSource;

public abstract class ExecutableCodeUnit extends CodeUnit {

	public ExecutableCodeUnit(
			ListMultimap<String, AbstractFunction> functionTable,
			CustomTypeGenerator type_generator) {
		super(functionTable, type_generator);
	}

	public ExecutableCodeUnit(Reader r,
			ListMultimap<String, AbstractFunction> functionTable,
			String sourcename, List<ScriptSource> includeDirectories,
			CustomTypeGenerator type_generator) throws ParsingException {
		super(r, functionTable, sourcename, includeDirectories, type_generator);
	}

	public abstract RuntimeExecutable<? extends ExecutableCodeUnit> run();
}
