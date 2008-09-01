package preprocessed;

import javax.naming.OperationNotSupportedException;

import processing.pascal_program;

public class dummy_declaration extends abstract_function {
	String name;

	Class[] arg_types;

	public dummy_declaration(String name, Class[] arg_types) {
		this.name = name;
		this.arg_types = arg_types;
	}
	@Override
	Class[] get_arg_types() {
		return arg_types;
	}

	@Override
	String get_name() {
		return name;
	}

	@Override
	public Class get_return_type() {
		new OperationNotSupportedException(
				"Dummy Declarations Do Not Have a Return Type")
				.printStackTrace();
		return null;
	}
	@Override
	public Object call(pascal_program program, Object[] arguments) {
		return null;
	}

}
