package com.js.interpreter.runtime.variables;

import java.lang.reflect.Array;

import com.js.interpreter.ast.instructions.returnsvalue.ReturnsValue;
import com.js.interpreter.pascaltypes.DeclaredType;
import com.js.interpreter.runtime.ArrayPointer;
import com.js.interpreter.runtime.VariableBoxer;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;

public class ReturnsValue_SubvarIdentifier implements SubvarIdentifier {
	ReturnsValue value;

	public ReturnsValue_SubvarIdentifier(ReturnsValue next_returns_value) {
		value = next_returns_value;
	}

	@Override
	public String toString() {
		return '[' + value.toString() + ']';
	}

	@Override
	public Object get(Object container, VariableContext f,
			RuntimeExecutable<?> main) {
		int index = -1;
		try {
			index = ((Number) value.get_value(f, main)).intValue();
		} catch (NullPointerException e) {
			System.err
					.println("Nullpointer exception in returnsvalue_subvaridentifier.get()  most likely, this is caused because a pointer was accidentally created to be context sensitive, i.e. it's pointed to location can change every time it is acessed.");
			System.exit(0);
			return null;
		}
		if (container instanceof StringBuilder) {
			return ((StringBuilder) container).charAt(index);
		} else {
			return Array.get(container, index);
		}

	}

	@Override
	public VariableBoxer create_pointer(Object container,
			VariableContext context, RuntimeExecutable<?> main) {
		return new ArrayPointer(container, ((Number) value.get_value(context,
				main)).intValue());
	}

	@Override
	public DeclaredType getType(DeclaredType containerType) {
		return (containerType.get_type_array()).element_type;
	}

	@Override
	public void set(Object container, VariableContext context,
			RuntimeExecutable<?> main, Object input) {
		int index = ((Number) value.get_value(context, main)).intValue();
		if (container instanceof StringBuilder) {
			((StringBuilder) container).setCharAt(index, (Character) input);
		} else {
			Array.set(container, index, input);
		}
	}

}
