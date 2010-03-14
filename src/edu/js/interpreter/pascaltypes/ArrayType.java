package edu.js.interpreter.pascaltypes;

import java.lang.reflect.Array;
import java.util.Arrays;

import edu.js.interpreter.preprocessed.FunctionDeclaration;
import edu.js.interpreter.preprocessed.instructions.returnsvalue.ReturnsValue;
import serp.bytecode.Code;

//This class gets the version 1.0 stamp of approval.  Hopefully I won't have to change it any more.
public class ArrayType extends PascalType {
	public final PascalType element_type;

	public final int[] lower_bounds;

	public final int[] array_sizes;

	public ArrayType(PascalType element_class, int[] lower, int[] sizes) {
		this.element_type = element_class;
		this.lower_bounds = lower;
		this.array_sizes = sizes;
	}

	@Override
	public boolean isarray() {
		return true;
	}

	/**
	 * This basically tells if the types are assignable from each other
	 * according to pascal.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj instanceof ArrayType) {
			ArrayType o = (ArrayType) obj;
			if (o.element_type.equals(element_type)
					&& o.lower_bounds.length == lower_bounds.length) {
				for (int i = 0; i < array_sizes.length; i++) {
					if (array_sizes[i] != 0) {
						return false;
					}
					if (lower_bounds[i] != 0) {
						return false;
					}
					if (o.array_sizes[i] != 0) {
						return false;
					}
					if (o.lower_bounds[i] != 0) {
						return false;
					}

				}
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		return (element_type.hashCode() * 31 + Arrays.hashCode(lower_bounds))
				* 31 + Arrays.hashCode(array_sizes);
	}
/**
 * TODO: Must make this actually fill in array with default values
 */
	@Override
	public Object initialize() {
		Object result= Array.newInstance(element_type.toclass(), array_sizes);
		Object default_value=element_type.initialize();
		//TODO loop through multidimensional array
		return result;
	}

	@Override
	public Class toclass() {
		int depth = lower_bounds.length;
		String s = element_type.toclass().getName();
		int length = s.length() + depth + 1;
		StringBuilder b = new StringBuilder(length);
		for (int i = 0; i < depth; i++) {
			b.append('[');
		}
		b.append('L');
		b.append(s);
		b.append(';');
		try {
			return Class.forName(b.toString());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
/**
 * TODO: Make this fill in array with default values.
 */
	@Override
	public void get_default_value_on_stack(Code code) {
		for (int i : array_sizes) {
			code.constant().setValue(i);
		}
		code.multianewarray().setDimensions(array_sizes.length).setType(
				element_type.toclass());
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder(element_type.toString());
		for (int i = 0; i < array_sizes.length; i++) {
			result.append('[');
			result.append(array_sizes[i]);
			result.append(']');
		}
		return result.toString();
	}

	/**
	 * This basically won't do any conversions, as array types have to be exact,
	 * except variable length arrays, but they are checked in the
	 * {@link array_type.equals(Object o)}.
	 */
	@Override
	public ReturnsValue convert(ReturnsValue value, FunctionDeclaration f) {
		PascalType other = value.get_type(f);
		if (other == this || this.equals(other)) {
			return value;
		}
		return null;
	}
}
