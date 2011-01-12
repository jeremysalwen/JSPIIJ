package com.js.interpreter.pascaltypes;

import com.js.interpreter.ast.ExpressionContext;
import com.js.interpreter.ast.instructions.returnsvalue.ReturnsValue;
import com.js.interpreter.exceptions.ExpectedTokenException;
import com.js.interpreter.exceptions.NonConstantExpressionException;
import com.js.interpreter.exceptions.ParsingException;
import com.js.interpreter.tokens.Token;
import com.js.interpreter.tokens.basic.PeriodToken;
import com.js.interpreter.tokens.grouping.GrouperToken;

public class SubrangeType {
	public SubrangeType() {
		this.lower = 0;
		this.size = 0;
	}

	public SubrangeType(GrouperToken i, ExpressionContext context)
			throws ParsingException {
		ReturnsValue low = JavaClassBasedType.Integer.convert(i
				.getNextExpression(context), context);
		Object min = low.compileTimeValue();
		if (min == null) {
			throw new NonConstantExpressionException(low);
		}
		lower = (Integer) min;

		Token t = i.take();
		if (!(t instanceof PeriodToken)) {
			throw new ExpectedTokenException("..", t);
		}
		t = i.take();
		if (!(t instanceof PeriodToken)) {
			throw new ExpectedTokenException("..", t);
		}
		ReturnsValue high = JavaClassBasedType.Integer.convert(i
				.getNextExpression(context), context);
		Object max = high.compileTimeValue();
		if (max == null) {
			throw new NonConstantExpressionException(high);
		}
		size = (((Integer) max) - lower) + 1;
	}

	public SubrangeType(int lower, int size) {
		this.lower = lower;
		this.size = size;
	}

	int lower;
	int size;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + lower;
		result = prime * result + size;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof SubrangeType))
			return false;
		SubrangeType other = (SubrangeType) obj;
		return lower == other.lower && size == other.size;
	}

	public boolean contains(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof SubrangeType))
			return false;
		SubrangeType other = (SubrangeType) obj;
		return lower <= other.lower
				&& (lower + size) >= (other.lower + other.size);
	}

	@Override
	public String toString() {
		return lower + ".." + (lower + size - 1);
	}
}
