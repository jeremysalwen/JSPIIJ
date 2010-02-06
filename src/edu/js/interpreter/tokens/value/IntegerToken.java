package edu.js.interpreter.tokens.value;


public class IntegerToken implements ValueToken {
	public int value;

	public IntegerToken(int i) {
		value = i;
	}

	@Override
	public String toString() {
		return "integer_value_of[" + value + ']';
	}
}
