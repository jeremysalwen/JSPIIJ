package edu.js.interpreter.tokens.value;


public class double_token implements value_token {
	public double value;

	public double_token(double d) {
		value = d;
	}

	@Override
	public String toString() {
		return "double_of_value[" + value + ']';
	}
}
