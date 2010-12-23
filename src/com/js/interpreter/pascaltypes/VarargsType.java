package com.js.interpreter.pascaltypes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.js.interpreter.ast.FunctionDeclaration;
import com.js.interpreter.ast.instructions.returnsvalue.ReturnsValue;
import com.js.interpreter.ast.instructions.returnsvalue.boxing.ArrayBoxer;
import com.js.interpreter.exceptions.ParsingException;
import com.js.interpreter.linenumber.LineInfo;

public class VarargsType implements ArgumentType {
	RuntimeType elementType;

	public VarargsType(RuntimeType elementType) {
		this.elementType = elementType;
	}

	@Override
	public ReturnsValue convertArgType(Iterator<ReturnsValue> args,
			FunctionDeclaration f) throws ParsingException {
		List<ReturnsValue> convertedargs = new ArrayList<ReturnsValue>();
		LineInfo line = null;
		while (args.hasNext()) {
			ReturnsValue tmp = elementType.convert(args.next(), f);
			line = tmp.getLineNumber();
			if (tmp == null) {
				return null;
			}
			convertedargs.add(tmp);
		}
		return new ArrayBoxer(
				convertedargs.toArray(new ReturnsValue[convertedargs.size()]),
				elementType, line);
	}

	@Override
	public Class getRuntimeClass() {
		return elementType.getClass();
	}

}
