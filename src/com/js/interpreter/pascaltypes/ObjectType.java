package com.js.interpreter.pascaltypes;

public abstract class ObjectType implements DeclaredType {

    public abstract DeclaredType getMemberType(String name);

}
