package com.js.interpreter.runtime.variables;

import java.lang.reflect.Array;

import com.js.interpreter.ast.CompileTimeContext;
import com.js.interpreter.ast.instructions.returnsvalue.ReturnsValue;
import com.js.interpreter.exceptions.ConstantCalculationException;
import com.js.interpreter.exceptions.NonArrayIndexed;
import com.js.interpreter.exceptions.ParsingException;
import com.js.interpreter.pascaltypes.DeclaredType;
import com.js.interpreter.pascaltypes.JavaClassBasedType;
import com.js.interpreter.runtime.ArrayPointer;
import com.js.interpreter.runtime.VariableBoxer;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.PascalIndexOutOfBoundsException;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public class ReturnsValue_SubvarIdentifier implements SubvarIdentifier {
	ReturnsValue value;
	int offset;

	public ReturnsValue_SubvarIdentifier(ReturnsValue next_returns_value,
			int offset) {
		value = next_returns_value;
		this.offset = offset;
	}

	@Override
	public String toString() {
		return '[' + value.toString() + ']';
	}

	@Override
	public Object get(Object container, VariableContext f,
			RuntimeExecutable<?> main) throws RuntimePascalException {
		return get(container, value.getValue(f, main));
	}

	Object get(Object container, Object indexvalue)
			throws RuntimePascalException {
		int index = (Integer) indexvalue;
		try {
			if (container instanceof StringBuilder) {
				/* pascal strings start indexing at 1 */
				return ((StringBuilder) container).charAt(index - 1);
			} else {
				return Array.get(container, index - offset);
			}
		} catch (IndexOutOfBoundsException e) {
			throwBoundsException(container, index);
			return null;
		}
	}

	private void throwBoundsException(Object container, int index)
			throws PascalIndexOutOfBoundsException {
		int min, max;
		if (container instanceof StringBuilder) {
			min = 1;
			max = ((StringBuilder) container).length();
		} else {
			min = offset;
			max = offset + Array.getLength(container) - 1;
		}
		throw new PascalIndexOutOfBoundsException(value.getLineNumber(), index,
				min, max);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public VariableBoxer create_pointer(Object container,
			VariableContext context, RuntimeExecutable<?> main)
			throws RuntimePascalException {
		return new ArrayPointer(container, ((Number) value.getValue(context,
				main)).intValue());
	}

	@Override
	public DeclaredType getType(DeclaredType containerType)
			throws ParsingException {
		if (containerType.isarray()) {
			return (containerType.get_type_array()).element_type;
		} else if (containerType == JavaClassBasedType.StringBuilder) {
			return JavaClassBasedType.Character;
		}
		throw new NonArrayIndexed(null, containerType);
	}

	@Override
	public void set(Object container, VariableContext context,
			RuntimeExecutable<?> main, Object input)
			throws RuntimePascalException {
		int index = (Integer) value.getValue(context, main);
		try {
			if (container instanceof StringBuilder) {
				((StringBuilder) container).setCharAt(index - 1,
						(Character) input);
			} else {
				Array.set(container, index - offset, input);
			}
		} catch (IndexOutOfBoundsException e) {
			throwBoundsException(container, index);
		}
	}

	@Override
	public Object compileTimeGet(Object container, CompileTimeContext context)
			throws ParsingException {
		Object index = value.compileTimeValue(context);
		if (value != null) {
			try {
				return get(container, index);
			} catch (RuntimePascalException e) {
				throw new ConstantCalculationException(e);
			}
		} else {
			return null;
		}
	}
}
