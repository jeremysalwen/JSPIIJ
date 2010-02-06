package edu.js.interpreter.tokens.value;


public class StringToken implements ValueToken {
	public String value;

	public StringToken(String s) {
		this.value = s;
	}

	@Override
	public String toString() {
		return new StringBuilder().append('"').append(value).append('"').toString();
	}
}
