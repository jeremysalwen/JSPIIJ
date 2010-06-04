package edu.js.interpreter.preprocessed.interpretingobjects.variables;

import java.lang.reflect.Array;

import com.sun.org.apache.bcel.internal.classfile.ConstantValue;
import com.sun.org.apache.regexp.internal.REUtil;

import edu.js.interpreter.pascaltypes.ArrayType;
import edu.js.interpreter.pascaltypes.PascalType;
import edu.js.interpreter.preprocessed.instructions.returnsvalue.ConstantAccess;
import edu.js.interpreter.preprocessed.instructions.returnsvalue.ReturnsValue;
import edu.js.interpreter.preprocessed.interpretingobjects.ArrayPointer;
import edu.js.interpreter.preprocessed.interpretingobjects.FunctionOnStack;
import edu.js.interpreter.preprocessed.interpretingobjects.Pointer;

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
	public Object get(Object container, FunctionOnStack f) {
		int index = -1;
		try {
			index = ((Number) value.get_value(f)).intValue();
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

	@SuppressWarnings("unchecked")
	@Override
	public Pointer create_pointer(Object container, FunctionOnStack context) {
		return new ArrayPointer(container, ((Number) value.get_value(context))
				.intValue());
	}

	@Override
	public PascalType getType(PascalType containerType) {
		return ((ArrayType) containerType).element_type;
	}

	@Override
	public void set(Object container, FunctionOnStack context, Object input) {
		int index = ((Number) value.get_value(context)).intValue();
		if (container instanceof StringBuilder) {
			((StringBuilder) container).setCharAt(index, (Character) input);
		} else {
			Array.set(container, index, input);
		}
	}

}
