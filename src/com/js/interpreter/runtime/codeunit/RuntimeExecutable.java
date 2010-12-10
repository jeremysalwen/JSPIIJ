package com.js.interpreter.runtime.codeunit;

import java.util.HashMap;
import java.util.Map;

import com.js.interpreter.ast.codeunit.ExecutableCodeUnit;
import com.js.interpreter.ast.codeunit.Library;

public abstract class RuntimeExecutable<parent extends ExecutableCodeUnit>
		extends RuntimeCodeUnit<parent> implements Runnable {
	Map<Library, RuntimeLibrary> RuntimeLibs = new HashMap<Library, RuntimeLibrary>();

	public RuntimeLibrary getLibrary(Library l) {
		RuntimeLibrary result = RuntimeLibs.get(l);
		if (result == null) {
			result = l.run();
			RuntimeLibs.put(l, result);
		}
		return result;
	}
	
}
