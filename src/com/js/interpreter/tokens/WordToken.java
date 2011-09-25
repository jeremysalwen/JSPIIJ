package com.js.interpreter.tokens;

import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.exceptions.ParsingException;
import com.js.interpreter.exceptions.UnrecognizedTypeException;
import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.pascaltypes.BasicType;
import com.js.interpreter.pascaltypes.DeclaredType;

public class WordToken extends Token {
	public String name;

	public WordToken(LineInfo line, String s) {
		super(line);
		this.name = s;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public WordToken get_word_value() throws ParsingException {
		return this;
	}

	public DeclaredType to_basic_type(ExpressionContext context)
			throws UnrecognizedTypeException {
		String s = name.toLowerCase().intern();
		if (s == "integer") {
			return BasicType.Integer;
		}
		if (s == "string") {
			return BasicType.StringBuilder;
		}
		if (s == "single" || s == "extended" || s == "double" || s == "real") {
			return BasicType.Double;
		}
		if (s == "long") {
			return BasicType.Long;
		}
		if (s == "boolean") {
			return BasicType.Boolean;
		}
		if (s == "character" || s == "char") {
			return BasicType.Character;
		}
		DeclaredType type = context.getTypedefType(s);
		if (type != null) {
			return type;
		} else {
			Object constval = context.getConstantDefinition(s);
			if (constval == null) {
				throw new UnrecognizedTypeException(lineInfo, s);
			}
			return BasicType.anew(constval.getClass());
		}
	}
}
