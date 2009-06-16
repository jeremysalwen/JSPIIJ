package edu.js.interpreter.pascal_types;

import java.util.HashMap;

import serp.bytecode.Code;

public abstract class pascal_type {
	public abstract Object initialize();

	public abstract boolean isarray();

	public abstract Class toclass();

	public array_type get_type_array() {
		return ((array_type) this);
	}

	public static pascal_type Integer = new class_pascal_type(Integer.class);

	public static pascal_type Double = new class_pascal_type(Double.class);

	public static pascal_type Long = new class_pascal_type(Long.class);

	public static pascal_type String = new class_pascal_type(String.class);

	public static pascal_type Character = new class_pascal_type(Character.class);

	public static pascal_type Float = new class_pascal_type(Float.class);

	public static pascal_type Boolean = new class_pascal_type(Boolean.class);

	static HashMap<pascal_type, Object> default_values = new HashMap<pascal_type, Object>();
	static {
		default_values.put(Integer, 0);
		default_values.put(String, "");
		default_values.put(Double, 0.0D);
		default_values.put(Long, 0L);
		default_values.put(Character, '\0');
		default_values.put(Float, 0.0F);
	}

	public abstract void get_default_value_on_stack(Code code);
}
