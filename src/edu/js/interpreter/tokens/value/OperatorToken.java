package edu.js.interpreter.tokens.value;


public class OperatorToken implements ValueToken {
	public OperatorTypes type;

	public OperatorToken(OperatorTypes t) {
		this.type = t;
	}

	public boolean can_be_unary() {
		switch (type) {
		case MINUS:
		case NOT:
		case PLUS:
			return true;
		default:
			return false;
		}
	}

	@Override
	public String toString() {
		return type.toString();
	}
}
