package com.js.interpreter.runtime.codeunit;

import java.util.HashMap;
import java.util.Map;

import com.js.interpreter.ast.codeunit.ExecutableCodeUnit;
import com.js.interpreter.ast.codeunit.Library;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public abstract class RuntimeExecutable<parent extends ExecutableCodeUnit>
		extends RuntimeCodeUnit<parent> {
			
	public RuntimeExecutable(parent definition) {
		super(definition);
	}

	Map<Library, RuntimeLibrary> RuntimeLibs = new HashMap<Library, RuntimeLibrary>();

	public RuntimeLibrary getLibrary(Library l) {
		RuntimeLibrary result = RuntimeLibs.get(l);
		if (result == null) {
			result = l.run();
			RuntimeLibs.put(l, result);
		}
		return result;
	}

	public abstract void run() throws RuntimePascalException;

}
