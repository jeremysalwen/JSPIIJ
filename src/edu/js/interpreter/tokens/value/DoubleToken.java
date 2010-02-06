package edu.js.interpreter.tokens.value;


public class DoubleToken implements ValueToken {
	public double value;

	public DoubleToken(double d) {
		value = d;
	}

	@Override
	public String toString() {
		return "double_of_value[" + value + ']';
	}
}
