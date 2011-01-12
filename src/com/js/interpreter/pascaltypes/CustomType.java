package com.js.interpreter.pascaltypes;

import java.util.ArrayList;
import java.util.List;

import serp.bytecode.Code;

import com.js.interpreter.ast.ExpressionContext;
import com.js.interpreter.ast.FunctionDeclaration;
import com.js.interpreter.ast.VariableDeclaration;
import com.js.interpreter.ast.instructions.returnsvalue.ReturnsValue;
import com.js.interpreter.exceptions.ParsingException;

public class CustomType extends DeclaredType {
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
	public boolean equals(DeclaredType obj) {
		if (!(obj instanceof CustomType)) {
			return false;
		}
		CustomType other = (CustomType) obj;
		return variable_types.equals(other.variable_types)
				&& name.equals(other.name);
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
	public ReturnsValue convert(ReturnsValue value, ExpressionContext f) throws ParsingException {
		RuntimeType other_type = value.get_type(f);
		if (this.equals(other_type)) {
			return value;
		}
		return null;
	}

	public DeclaredType getMemberType(String name) {
		for (VariableDeclaration v : variable_types) {
			if (v.name.equals(name)) {
				return v.type;
			}
		}
		System.err.println("Could not find member " + name);
		return null;
	}

	@Override
	public void pushDefaultValue(Code constructor_code) {
		// TODO Auto-generated method stub
		
	}
}
