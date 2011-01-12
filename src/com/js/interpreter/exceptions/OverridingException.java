package com.js.interpreter.exceptions;

import com.js.interpreter.ast.AbstractFunction;
import com.js.interpreter.ast.VariableDeclaration;
import com.js.interpreter.linenumber.LineInfo;

public class OverridingException extends ParsingException {

	public OverridingException(AbstractFunction f,
			String constantName, LineInfo line) {
		super(line, "Cannot define constant "+constantName+". There is already a function "+f.toString()
				+ " which was previous defined with this name.");
	}

	public OverridingException(
			String constantName, AbstractFunction f, LineInfo line) {
		super(line, "Cannot define function "+f.toString()+". There is already a constant "+constantName
				+ " which was previous defined with this name.");
	}
	
	public OverridingException(
			String constantName, VariableDeclaration v, LineInfo line) {
		super(line, "Cannot define variable "+v.name+". There is already a constant "+constantName
				+ " which was previous defined with this name.");
	}
	public OverridingException(
			String constantName, String constantName2, LineInfo line) {
		super(line, "Cannot define constant "+constantName+". There is already another constant "+constantName2
				+ " which was previous defined.");
	}

	public OverridingException(VariableDeclaration v, AbstractFunction f,
			LineInfo line) {
		super(line, "Cannot define function "+f.toString()+". There is already a variable "+v.name
				+ " which was previous defined.");
	}

	public OverridingException(VariableDeclaration v, String conststant,
			LineInfo line) {
		super(line, "Cannot define variable "+v.name+". There is already a constant "+conststant
				+ " which was previous defined.");
	}

	public OverridingException(VariableDeclaration old, VariableDeclaration n, LineInfo line) {
		super(line, "Cannot define variabele "+n.name+". There is already a variable " + old.name
				+ " which was previous define at " + line.line);
	}

	public OverridingException(AbstractFunction v, VariableDeclaration f,
			LineInfo line) {
		super(line, "Cannot define variable "+f.name+". There is already a function "+v.toString()
				+ " which was previous defined.");
	}

	public OverridingException(AbstractFunction v, AbstractFunction f,
			LineInfo line) {
		super(line, "Cannot define "+f.toString()+". There is already a function "+f.toString()
				+ " which was previous defined.");
	}

}
