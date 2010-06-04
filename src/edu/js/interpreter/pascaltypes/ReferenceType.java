package edu.js.interpreter.pascaltypes;

import serp.bytecode.Code;
import edu.js.interpreter.preprocessed.FunctionDeclaration;
import edu.js.interpreter.preprocessed.instructions.returnsvalue.CreatePointer;
import edu.js.interpreter.preprocessed.instructions.returnsvalue.ReturnsValue;
import edu.js.interpreter.preprocessed.instructions.returnsvalue.VariableAccess;
import edu.js.interpreter.preprocessed.interpretingobjects.Pointer;
import edu.js.interpreter.preprocessed.interpretingobjects.variables.VariableIdentifier;

public class ReferenceType extends PascalType {
	public PascalType child_type;

	public ReferenceType(PascalType child) {
		this.child_type = child;
	}

	@Override
	public void get_default_value_on_stack(Code code) {
		System.err
				.println("Tried to generate bytecode to initialize variable of reference type");
		System.exit(1);
	}

	@Override
	public Object initialize() {
		System.err.println("Tried to initialize variable of reference type");
		System.exit(1);
		return null;
	}

	@Override
	public boolean isarray() {
		return false;
	}

	@Override
	public Class toclass() {
		return Pointer.class;
	}

	@Override
	public ReturnsValue convert(ReturnsValue value, FunctionDeclaration f) {
		if (value instanceof VariableAccess) {
			VariableAccess access = (VariableAccess) value;
			VariableIdentifier identifier = new VariableIdentifier();
			for (int i = 0; i < access.variable_name.size() - 1; i++) {
				identifier.add(access.variable_name.get(i));
			}
			VariableAccess new_access = new VariableAccess(identifier);
			return new CreatePointer(new_access, access.variable_name
					.get(access.variable_name.size() - 1));
		}
		return null;
	}

}
