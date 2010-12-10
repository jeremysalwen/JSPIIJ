package com.js.interpreter.pascaltypes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.js.interpreter.ast.FunctionDeclaration;
import com.js.interpreter.ast.instructions.returnsvalue.ReturnsValue;
import com.js.interpreter.ast.instructions.returnsvalue.boxing.ArrayBoxer;
import com.js.interpreter.exceptions.ParsingException;

public class VarargsType implements ArgumentType {
	RuntimeType elementType;

	public VarargsType(RuntimeType elementType) {
		this.elementType = elementType;
	}

	@Override
	public ReturnsValue convert(Iterator<ReturnsValue> args,
			FunctionDeclaration f) throws ParsingException {
		List<ReturnsValue> convertedargs = new ArrayList<ReturnsValue>();
		while (args.hasNext()) {
			ReturnsValue tmp = elementType.convert(args, f);
			if (tmp == null) {
				return null;
			}
			convertedargs.add(tmp);
		}
		return new ArrayBoxer(
				convertedargs.toArray(new ReturnsValue[convertedargs.size()]),
				elementType);
	}

	@Override
	public Class getRuntimeClass() {
		return elementType.getClass();
	}

	@Override
	public boolean equals(ArgumentType other) {
		if (other instanceof VarargsType) {
			VarargsType varargs = (VarargsType) other;
			return varargs.elementType.equals(elementType);
		}
		return false;
	}
}
