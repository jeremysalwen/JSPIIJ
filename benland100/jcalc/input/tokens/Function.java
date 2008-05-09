package benland100.jcalc.input.tokens;

import java.util.ArrayList;

import benland100.jcalc.input.Token;

import pascalTypes.Double;
import pascalTypes.pascalType;
import preProcessing.PascalProgram;
import preProcessing.ReturningProcessedObject;
import processing.CodeProcessor;

public class Function extends Token {

	private String name;

	public Function(String name, PascalProgram p) {
		if (p.getFunction(name) == null)
			throw new RuntimeException("Unknown Function: " + name);
		this.name = name;
	}

	public double evaluate(ReturningProcessedObject[] args, CodeProcessor c) {
		ArrayList<pascalType> arg = new ArrayList<pascalType>();
		for (ReturningProcessedObject r : args)
			arg.add(r.getValue(c));
		return ((Double) c.getPluginVal(name, arg)).get();
	}

	public boolean isVarargs() {
		return true;
	}

	public String toString() {
		return name;
	}

}
