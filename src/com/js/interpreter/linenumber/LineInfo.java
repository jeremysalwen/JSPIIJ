package com.js.interpreter.linenumber;

public class LineInfo {
	public int line;
	public int column = -1;
	public String sourcefile;

	public LineInfo(int line, String sourcefile) {
		this.line = line;
		this.sourcefile = sourcefile;
	}

	public LineInfo(int line, int column, String sourcefile) {
		this.line = line;
		this.column = column;
		this.sourcefile = sourcefile;
	}

	@Override
	public String toString() {
		return sourcefile + ":" + (column >= 0 ? column + ":" : "") + line;

	}
}
