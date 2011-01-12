package com.js.interpreter.pascaltypes;

import java.lang.reflect.Array;

import serp.bytecode.Code;

import com.js.interpreter.ast.ExpressionContext;
import com.js.interpreter.ast.FunctionDeclaration;
import com.js.interpreter.ast.instructions.returnsvalue.ReturnsValue;
import com.js.interpreter.exceptions.ParsingException;

//This class gets the version 1.0 stamp of approval.  Hopefully I won't have to change it any more.
public class ArrayType<T extends DeclaredType> extends DeclaredType {
	public final T element_type;

	SubrangeType bounds;

	public ArrayType(T element_class, SubrangeType bounds) {
		this.element_type = element_class;
		this.bounds = bounds;
	}

	@Override
	public boolean isarray() {
		return true;
	}

	/**
	 * This basically tells if the types are assignable from each other
	 * according to Pascal.
	 */
	public boolean superset(DeclaredType obj) {
		if (this == obj) {
			return true;
		}
		if (obj instanceof ArrayType) {
			ArrayType<?> o = (ArrayType<?>) obj;
			if (o.element_type.equals(element_type)) {
				if (this.bounds.contains(o.bounds)) {
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean equals(DeclaredType obj) {
		if (this == obj) {
			return true;
		}
		if (obj instanceof ArrayType) {
			ArrayType<?> o = (ArrayType<?>) obj;
			if (o.element_type.equals(element_type)) {
				if (this.bounds.equals(o.bounds)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		return (element_type.hashCode() * 31 + bounds.hashCode());
	}

	/**
	 * TODO: Must make this actually fill in array with default values
	 */
	@Override
	public Object initialize() {
		Object result = Array.newInstance(element_type.toclass(), bounds.size);
		for (int i = 0; i < bounds.size; i++) {
			Array.set(result, i, element_type.initialize());
		}
		return result;
	}

	@Override
	public Class<?> toclass() {
		String s = element_type.toclass().getName();
		StringBuilder b = new StringBuilder();
		b.append('[');
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
	public String toString() {
		StringBuilder result = new StringBuilder(element_type.toString());

		result.append('[');
		result.append(bounds);
		result.append(']');
		return result.toString();
	}

	/**
	 * This basically won't do any conversions, as array types have to be exact,
	 * except variable length arrays, but they are checked in the {@link
	 * array_type.equals(Object o)}.
	 */
	@Override
	public ReturnsValue convert(ReturnsValue value, ExpressionContext f) throws ParsingException{
		RuntimeType other = value.get_type(f);
		
		return this.superset(other.declType) ? value : null;
	}

	@Override
	public void pushDefaultValue(Code constructor_code) {
		// TODO Auto-generated method stub
		
	}
}
