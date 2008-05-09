package benland100.jcalc.input.tokens;

import benland100.jcalc.input.Token;
import preProcessing.PascalProgram;
import preProcessing.PreProcessedOnly.PreprocessedFunction;
import processing.CodeProcessor;

public class Variable<T> extends Token {

	String name;

	public Variable(String name, PreprocessedFunction p) {
		if (p.getVariableType(name) == null)
			throw new RuntimeException("Variable not found: " + name);
		this.name = name;
	}

	@SuppressWarnings("unchecked")
	public T getValue(CodeProcessor p) {
		return (T) p.getVar(name);
	}

	public String getName() {
		return name;
	}

	public String toString() {
		return name;
	}
}