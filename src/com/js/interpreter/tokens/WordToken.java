package com.js.interpreter.tokens;

import com.js.interpreter.ast.ExpressionContext;
import com.js.interpreter.exceptions.ParsingException;
import com.js.interpreter.exceptions.UnrecognizedTypeException;
import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.pascaltypes.DeclaredType;
import com.js.interpreter.pascaltypes.JavaClassBasedType;

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
			return JavaClassBasedType.Integer;
		}
		if (s == "string") {
			return JavaClassBasedType.StringBuilder;
		}
		if (s == "single" || s == "extended" || s == "double" || s == "real") {
			return JavaClassBasedType.Double;
		}
		if (s == "long") {
			return JavaClassBasedType.Long;
		}
		if (s == "boolean") {
			return JavaClassBasedType.Boolean;
		}
		if (s == "character" || s == "char") {
			return JavaClassBasedType.Character;
		}
		DeclaredType type = context.getTypedefType(s);
		if (type != null) {
			return type;
		} else {
			Object constval = context.getConstantDefinition(s);
			if (constval == null) {
				throw new UnrecognizedTypeException(lineInfo, s);
			}
			return JavaClassBasedType.anew(constval.getClass());
		}
	}
}
