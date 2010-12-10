package com.js.interpreter.runtime.codeunit;

import java.util.HashMap;
import java.util.Map;

import com.js.interpreter.ast.codeunit.Library;
import com.js.interpreter.runtime.VariableContext;

public class RuntimeLibrary extends RuntimeCodeUnit<Library> {
	Library l;
	Map<String, Object> UnitVariables = new HashMap<String, Object>();

	public RuntimeLibrary(Library l) {
		this.l = l;
	}

	@Override
	public Library getDefinition() {
		return l;
	}

	@Override
	public Object getLocalVar(String name) {
		return UnitVariables.get(name);
	}

	@Override
	public boolean setLocalVar(String name, Object val) {
		return UnitVariables.put(name, val) != null;
	}

	@Override
	public VariableContext getParentContext() {
		return null;
	}

}
