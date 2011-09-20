package com.js.interpreter.pascaltypes;

import java.util.List;

import serp.bytecode.Code;

import com.js.interpreter.ast.ExpressionContext;
import com.js.interpreter.ast.returnsvalue.ReturnsValue;
import com.js.interpreter.exceptions.NonArrayIndexed;
import com.js.interpreter.exceptions.ParsingException;
import com.js.interpreter.pascaltypes.bytecode.RegisterAllocator;
import com.js.interpreter.pascaltypes.bytecode.TransformationInput;

public interface DeclaredType {

	public abstract Object initialize();

	public abstract Class getTransferClass();


	public abstract ReturnsValue convert(ReturnsValue returns_value,
			ExpressionContext f) throws ParsingException;

	public abstract boolean equals(DeclaredType other);

	public abstract void pushDefaultValue(Code constructor_code,
			RegisterAllocator ra);

	public abstract void cloneValueOnStack(TransformationInput t);

	public abstract ReturnsValue cloneValue(ReturnsValue r);

	public abstract ReturnsValue generateArrayAccess(ReturnsValue array,
			ReturnsValue index) throws NonArrayIndexed;

	public abstract Class<?> getStorageClass();

	public abstract void arrayStoreOperation(Code c);

	public abstract void convertStackToStorageType(Code c);

	public abstract void pushArrayOfType(Code code, RegisterAllocator ra,
			List<SubrangeType> ranges);

}