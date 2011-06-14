package com.js.interpreter.pascaltypes;

import java.util.List;

import serp.bytecode.Code;
import serp.bytecode.Instruction;
import serp.bytecode.JumpInstruction;

import com.js.interpreter.ast.ExpressionContext;
import com.js.interpreter.ast.returnsvalue.ReturnsValue;
import com.js.interpreter.exceptions.NonArrayIndexed;
import com.js.interpreter.exceptions.ParsingException;
import com.js.interpreter.pascaltypes.bytecode.RegisterAllocator;
import com.js.interpreter.pascaltypes.bytecode.ScopedRegisterAllocator;
import com.js.interpreter.pascaltypes.bytecode.TransformationInput;

public abstract class DeclaredType {
	public abstract Object initialize();

	public abstract boolean isarray();

	public abstract Class getTransferClass();

	@SuppressWarnings("rawtypes")
	public ArrayType get_type_array() {
		return ((ArrayType) this);
	}

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

	public void pushArrayOfType(Code code, RegisterAllocator ra,
			List<SubrangeType> ranges) {
		for (SubrangeType i : ranges) {
			code.constant().setValue(i.size);
		}
		// For now we are storing as array of objects always
		code.multianewarray().setDimensions(ranges.size())
				.setType("[" + this.getTransferClass().getName());
		int array = ra.getNextFree();
		code.astore().setLocal(array);
		int[] indexes = new int[ranges.size()];
		JumpInstruction[] fjmp = new JumpInstruction[ranges.size()];
		Instruction[] jmpbackto = new Instruction[ranges.size() + 1];
		for (int i = 0; i < indexes.length; i++) {
			indexes[i] = ra.getNextFree();
			jmpbackto[i] = code.constant().setValue(0);
			code.istore().setLocal(indexes[i]);
			fjmp[i] = code.go2();
		}
		jmpbackto[ranges.size()] = code.aload().setLocal(array);
		for (int i = 0; i < indexes.length - 1; i++) {
			code.iload().setLocal(indexes[i]);
			code.aaload();
		}
		code.iload().setLocal(indexes[indexes.length - 1]);
		pushDefaultValue(code, new ScopedRegisterAllocator(ra));
		convertStackToStorageType(code);
		// Because we are not compiling yet, we are using wrapper classes in
		// arrays.
		// arrayStoreOperation(code);
		code.aastore();
		for (int i = 0; i < indexes.length; i++) {
			code.iinc().setIncrement(1).setLocal(indexes[i]);
			fjmp[i].setTarget(code.iload().setLocal(indexes[i]));
			code.constant().setValue(ranges.get(i).size);
			code.ificmplt().setTarget(jmpbackto[i + 1]);
		}
		for (int i = 0; i < indexes.length; i++) {
			ra.free(indexes[i]);
		}
		code.aload().setLocal(array);
		ra.free(array);
	}
}