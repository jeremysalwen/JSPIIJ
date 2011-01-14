package com.js.interpreter.tokens.grouping;

import java.util.ArrayList;
import java.util.List;

import com.js.interpreter.ast.ExpressionContext;
import com.js.interpreter.ast.instructions.returnsvalue.ReturnsValue;
import com.js.interpreter.exceptions.ExpectedTokenException;
import com.js.interpreter.exceptions.ParsingException;
import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.tokens.Token;
import com.js.interpreter.tokens.basic.CommaToken;

public class ParenthesizedToken extends GrouperToken {
	public ParenthesizedToken(LineInfo line) {
		super(line);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 3945938644412769985L;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("(");
		if (next != null) {
			builder.append(next).append(',');
		}
		for (Token t : this.queue) {
			builder.append(t).append(' ');
		}
		builder.append(')');
		return builder.toString();
	}

	public List<ReturnsValue> get_arguments_for_call(ExpressionContext context)
			throws ParsingException {
		List<ReturnsValue> result = new ArrayList<ReturnsValue>();
		while (hasNext()) {
			result.add(getNextExpression(context));
			if (hasNext()) {
				Token next = take();
				if (!(next instanceof CommaToken)) {
					throw new ExpectedTokenException(",", next);
				}
			}
		}
		return result;
	}

	@Override
	protected String getClosingText() {
		return ")";
	}
}
