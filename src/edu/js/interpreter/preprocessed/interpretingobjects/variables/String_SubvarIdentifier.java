package edu.js.interpreter.preprocessed.interpretingobjects.variables;

import edu.js.interpreter.pascaltypes.CustomType;
import edu.js.interpreter.pascaltypes.PascalType;
import edu.js.interpreter.preprocessed.interpretingobjects.ContainsVariablesPointer;
import edu.js.interpreter.preprocessed.interpretingobjects.FunctionOnStack;
import edu.js.interpreter.preprocessed.interpretingobjects.Pointer;

public class String_SubvarIdentifier implements SubvarIdentifier {
	String s;

	public String_SubvarIdentifier(String s) {
		this.s = s;
	}

	@Override
	public String toString() {
		return s;
	}

	@Override
	public Object get(Object container, FunctionOnStack context) {
		return ((ContainsVariables) container).get_var(s);
	}

	@Override
	public Pointer create_pointer(Object container, FunctionOnStack context) {
		return new ContainsVariablesPointer((ContainsVariables) container, s);
	}

	@Override
	public PascalType getType(PascalType containerType) {
		if (!(containerType instanceof CustomType)) {
			System.err
					.println("Error! Tried to find subitem's class of container object with a string index, when that's not applicable");
		}
		return ((CustomType) containerType).getMemberType(this.s);
	}

	@Override
	public void set(Object container, FunctionOnStack context, Object value) {
		((ContainsVariables) container).set_var(this.s, value);
	}

}
