package edu.js.interpreter.pascal_types;

import java.util.ArrayList;
import java.util.List;

import edu.js.interpreter.preprocessed.variable_declaration;

import serp.bytecode.Code;

public class custom_type_declaration extends pascal_type {
	/**
	 * This class represents a declaration of a new type in pascal.
	 */

	public String name;

	/**
	 * This is a list of the defined variables in the custom type.
	 */
	public List<variable_declaration> variable_types;

	public custom_type_declaration() {
		variable_types = new ArrayList<variable_declaration>();
	}

	/**
	 * Adds another sub-variable to this user defined type.
	 * 
	 * @param v
	 *            The name and type of the variable to add.
	 */
	public void add_variable_declaration(variable_declaration v) {
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
		if (!(obj instanceof custom_type_declaration)) {
			return false;
		}
		return variable_types.equals((custom_type_declaration) obj)
				&& name.equals((custom_type_declaration) obj);
	}

	@Override
	public Class toclass() {
		try {
			return Class.forName("edu.js.interpreter.dynamic_types." + hashCode());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}