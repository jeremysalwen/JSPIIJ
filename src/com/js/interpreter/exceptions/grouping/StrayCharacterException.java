package com.js.interpreter.exceptions.grouping;

import com.js.interpreter.linenumber.LineInfo;

public class StrayCharacterException extends GroupingException {

	public StrayCharacterException(LineInfo line, char character) {
		super(line, "Stray character in program: <" + character
				+ ">, char code " + (int) character);
	}

}
