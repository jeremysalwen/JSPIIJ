package com.js.interpreter.linenumber;

public class LineInfo {
	public int line;
	public String sourcefile;

	public LineInfo(int line, String sourcefile) {
		this.line = line;
		this.sourcefile = sourcefile;
	}

	@Override
	public String toString() {
		return sourcefile + ':' + line;
	}
}
