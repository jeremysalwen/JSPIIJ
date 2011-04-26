package com.js.interpreter.pascaltypes;

import java.lang.reflect.Array;

import serp.bytecode.Code;
import serp.bytecode.JumpInstruction;

import com.js.interpreter.ast.CompileTimeContext;
import com.js.interpreter.ast.ExpressionContext;
import com.js.interpreter.ast.instructions.SetValueExecutable;
import com.js.interpreter.ast.instructions.returnsvalue.ArrayAccess;
import com.js.interpreter.ast.instructions.returnsvalue.ReturnsValue;
import com.js.interpreter.exceptions.ParsingException;
import com.js.interpreter.exceptions.UnassignableTypeException;
import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.pascaltypes.bytecode.TransformationInput;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

//This class gets the version 1.0 stamp of approval.  Hopefully I won't have to change it any more.
public class ArrayType<T extends DeclaredType> extends DeclaredType {
	public final T element_type;

	public SubrangeType bounds;

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
	public ReturnsValue convert(ReturnsValue value, ExpressionContext f)
			throws ParsingException {
		RuntimeType other = value.get_type(f);
		return this.superset(other.declType) ? cloneValue(value) : null;
	}

	@Override
	public void pushDefaultValue(Code constructor_code) {

	}

	@Override
	public ReturnsValue cloneValue(final ReturnsValue r) {
		return new ReturnsValue() {

			@Override
			public RuntimeType get_type(ExpressionContext f)
					throws ParsingException {
				return r.get_type(f);
			}

			@Override
			public Object getValue(VariableContext f, RuntimeExecutable<?> main)
					throws RuntimePascalException {
				Object[] value = (Object[]) r.getValue(f, main);
				return value.clone();
			}

			@Override
			public LineInfo getLineNumber() {
				return r.getLineNumber();
			}

			@Override
			public Object compileTimeValue(CompileTimeContext context)
					throws ParsingException {
				Object[] value = (Object[]) r.compileTimeValue(context);
				return value.clone();
			}

			@Override
			public SetValueExecutable createSetValueInstruction(ReturnsValue r)
					throws UnassignableTypeException {
				throw new UnassignableTypeException(this);
			}
		};
	}

	private void pushLengthOnStack(Code c, int varindex) {
		if (varindex >= 0) {
			c.iload().setLocal(varindex);
		} else {
			c.constant().setValue(bounds.size);
		}
	}

	@Override
	public void cloneValueOnStack(final TransformationInput t) {
		final Code c = t.getCode();
		t.pushInputOnStack();
		int varindex = -1;
		if (this.bounds.size == 0) {
			varindex = t.getFreeRegister();
			c.dup();
			c.arraylength();
			c.istore().setLocal(varindex);
		}
		// STACK=OLD
		final int cloneeindex = t.getFreeRegister();
		c.astore().setLocal(cloneeindex);
		pushLengthOnStack(c, varindex);
		// STACK=LEN
		c.dup();
		// STACK=LEN,LEN
		c.anewarray().setType(this.element_type.toclass());
		// Stack=LEN,NEW
		c.dupx1();
		c.swap();
		// STACK=NEW,NEW,LEN
		final int index = t.getFreeRegister();
		c.constant().setValue(0);
		c.dup();
		c.istore().setLocal(index);
		// STACK= NEW,NEW,LEN,IND
		c.dupx1();
		// STACK=NEW,NEW,IND,LEN,IND
		JumpInstruction jmp = c.ificmple();
		// STACK=NEW,NEW,IND
		element_type.cloneValueOnStack(new TransformationInput() {

			@Override
			public void pushInputOnStack() {
				c.aload().setLocal(cloneeindex);
				c.iload().setLocal(index);
				c.aload();
			}

			@Override
			public int getFreeRegister() {
				return t.getFreeRegister();
			}

			@Override
			public Code getCode() {
				return c;
			}

			@Override
			public void freeRegister(int index) {
				t.freeRegister(index);
			}
		});
		// STACK=NEW,NEW,IND,NEWVALUE
		c.aastore();
		// STACK=NEW
		c.dup();
		pushLengthOnStack(c, varindex);
		c.iload().setLocal(index);
		c.iinc();
		// STACK=NEW,NEW,LEN,IND
		c.jsr().setTarget(jmp);
		jmp.setTarget(c.pop2());
		c.pop2();
		t.freeRegister(cloneeindex);
		t.freeRegister(index);
		if (varindex != -1) {
			t.freeRegister(varindex);
		}
	}

	@Override
	public ReturnsValue generateArrayAccess(ReturnsValue array,
			ReturnsValue index) {
		return new ArrayAccess(array, index);
	}
}
