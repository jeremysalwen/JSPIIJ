package edu.js.interpreter.tokens.value;



public class WordToken implements ValueToken {
	public String name;

	public WordToken(String s) {
		this.name = s;
	}

	@Override
	public String toString() {
		return "word_[" + name + ']';
	}
}
