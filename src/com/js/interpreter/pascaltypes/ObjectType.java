package com.js.interpreter.pascaltypes;

public abstract class ObjectType extends DeclaredType {

	public abstract DeclaredType getMemberType(String name);
	
}
