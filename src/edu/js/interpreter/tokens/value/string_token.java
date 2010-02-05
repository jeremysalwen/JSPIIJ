package edu.js.interpreter.tokens.value;


public class string_token implements value_token {
	public String value;

	public string_token(String s) {
		this.value = s;
	}

	@Override
	public String toString() {
		return new StringBuilder().append('"').append(value).append('"').toString();
	}
}
