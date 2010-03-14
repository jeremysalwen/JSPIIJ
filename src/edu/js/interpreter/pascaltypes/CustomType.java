package edu.js.interpreter.pascaltypes;

import java.util.ArrayList;
import java.util.List;

import edu.js.interpreter.preprocessed.FunctionDeclaration;
import edu.js.interpreter.preprocessed.VariableDeclaration;
import edu.js.interpreter.preprocessed.instructions.returnsvalue.ReturnsValue;

import serp.bytecode.Code;

public class CustomType extends PascalType {
	/**
	 * This class represents a declaration of a new type in pascal.
	 */

	public String name;

	/**
	 * This is a list of the defined variables in the custom type.
	 */
	public List<VariableDeclaration> variable_types;

	public CustomType() {
		variable_types = new ArrayList<VariableDeclaration>();
	}

	/**
	 * Adds another sub-variable to this user defined type.
	 * 
	 * @param v
	 *            The name and type of the variable to add.
	 */
	public void add_variable_declaration(VariableDeclaration v) {
		variable_types.add(v);
	}

	@Override
	public void get_default_value_on_stack(Code code) {
		try {
			code.invokespecial().setMethod(toclass().getConstructor());
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Object initialize() {
		try {
			return toclass().newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean isarray() {
		return false;
	}

	@Override
	public int hashCode() {
		return name.hashCode() * 31 + variable_types.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof CustomType)) {
			return false;
		}
		return variable_types.equals(obj) && name.equals(obj);
	}

	@Override
	public Class toclass() {
		try {
			return Class.forName("edu.js.interpreter.custom_types."
					+ hashCode());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ReturnsValue convert(ReturnsValue value, FunctionDeclaration f) {
		PascalType other_type = value.get_type(f);
		if (this.equals(other_type)) {
			/**
			 * technically this should be == to conform to pascal specs, but we
			 * can be a bit more lenient
			 */
			return value;
		}
		return null;
	}

	public PascalType getMemberType(String name) {
		for (VariableDeclaration v : variable_types) {
			if (v.name.equals(name)) {
				return v.type;
			}
		}
		System.err.println("Could not find member "+name);
		return null;
	}
}