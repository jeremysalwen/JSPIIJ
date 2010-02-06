package edu.js.interpreter.preprocessed.interpretingobjects.variables;

public class String_SubvarIdentifier extends SubvarIdentifier {
	String s;

	public String_SubvarIdentifier(String s) {
		this.s = s;
	}

	@Override
	public boolean isreturnsvalue() {
		return false;
	}
@Override
public String toString() {
	return s;
}
}
