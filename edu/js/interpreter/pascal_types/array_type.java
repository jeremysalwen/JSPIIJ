package edu.js.interpreter.pascal_types;

import java.lang.reflect.Array;
import java.util.Arrays;

import edu.js.interpreter.tokens.value.integer_token;

import serp.bytecode.Code;

//This class gets the version 1.0 stamp of approval.  Hopefully I won't have to change it any more.
public class array_type extends pascal_type {
	public pascal_type element_type;

	public int[] lower_bounds;

	public int[] array_sizes;

	public array_type(pascal_type element_class, int[] lower, int[] sizes) {
		this.element_type = element_class;
		this.lower_bounds = lower;
		this.array_sizes = sizes;
	}

	@Override
	public boolean isarray() {
		return true;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof array_type) {
			array_type o = (array_type) obj;
			if (o.element_type.equals(element_type)
					&& o.lower_bounds.length == lower_bounds.length
					&& Arrays.equals(lower_bounds, o.lower_bounds)
					&& Arrays.equals(array_sizes, o.array_sizes)) {
				return true;
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		return (element_type.hashCode() * 31 + Arrays.hashCode(lower_bounds))
				* 31 + Arrays.hashCode(array_sizes);
	}

	@Override
	public Object initialize() {
		return Array.newInstance(element_type.toclass(), array_sizes);
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
}
