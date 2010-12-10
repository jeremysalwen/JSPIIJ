package com.js.interpreter.ast.codeunit;

import java.io.Reader;
import java.util.List;

import com.google.common.collect.ListMultimap;
import com.js.interpreter.ast.AbstractFunction;
import com.js.interpreter.classgeneration.CustomTypeGenerator;
import com.js.interpreter.exceptions.ParsingException;
import com.js.interpreter.runtime.codeunit.RuntimeLibrary;
import com.js.interpreter.startup.ScriptSource;

public class Library extends CodeUnit {
	public Library(ListMultimap<String, AbstractFunction> functionTable,
			CustomTypeGenerator type_generator) throws ParsingException {
		super(functionTable, type_generator);
	}

	public Library(Reader program,
			ListMultimap<String, AbstractFunction> functionTable,
			String sourcename, List<ScriptSource> includeDirectories,
			CustomTypeGenerator type_generator) throws ParsingException {
		super(program, functionTable, sourcename, includeDirectories,
				type_generator);
	}

	@Override
	public RuntimeLibrary run() {
		return new RuntimeLibrary(this);
	}

}
