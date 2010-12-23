package com.js.interpreter.runtime.exception;

import com.js.interpreter.linenumber.LineInfo;

public class PluginReflectionException extends RuntimePascalException {
	Exception e;

	public PluginReflectionException(LineInfo line, Exception cause) {
		this.e = cause;
		this.line = line;
	}

	@Override
	public String getMessage() {
		return "Internal Interpreter error occured when attempting to use reflection: "
				+ e.getMessage();
	}
}
