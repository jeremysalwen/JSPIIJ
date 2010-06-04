package edu.js.interpreter.pascaltypes;

import serp.bytecode.Code;
import edu.js.interpreter.preprocessed.FunctionDeclaration;
import edu.js.interpreter.preprocessed.instructions.returnsvalue.ReturnsValue;
import edu.js.interpreter.preprocessed.interpretingobjects.ObjectBasedPointer;
import edu.js.interpreter.preprocessed.interpretingobjects.Pointer;

public class PointerType extends PascalType {
	PascalType pointedToType;

	public PointerType(PascalType pointedToType) {
		this.pointedToType = pointedToType;
	}

	@Override
	public ReturnsValue convert(ReturnsValue returnsValue, FunctionDeclaration f) {
		PascalType other = returnsValue.get_type(f);
		if (this.equals(other)) {
			return returnsValue;
		}
		return null;
	}

	@Override
	public void get_default_value_on_stack(Code code) {
		pointedToType.get_default_value_on_stack(code);
		try {
			code.invokespecial().setMethod(Pointer.class.getConstructor());
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			System.err
					.println("WTF could not find the Pointer class's constructor.");
			e.printStackTrace();
		}
	}

	@Override
	public Object initialize() {
		return new ObjectBasedPointer();
	}

	@Override
	public boolean isarray() {
		return false;
	}

	@Override
	public Class toclass() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return super.equals(obj);
	}

}
