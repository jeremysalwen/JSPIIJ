package pascal_types;

import java.util.HashMap;

public class pascal_type {
	final Object reference;
	boolean is_custom_type;
	public static final pascal_type INTEGER = new pascal_type(Integer.class);
	public static final pascal_type DOUBLE = new pascal_type(Double.class);
	public static final pascal_type BOOLEAN = new pascal_type(Boolean.class);
	public static final pascal_type SINGLE = new pascal_type(Float.class);
	public static final pascal_type STRING = new pascal_type(String.class);
	public static final pascal_type LONG = new pascal_type(Long.class);
	public static final pascal_type CHAR = new pascal_type(Character.class);
	static HashMap<Class, Object> default_values;
	static {
		default_values = new HashMap<Class, Object>();
		default_values.put(Integer.class, 0);
		default_values.put(Double.class, 0.0D);
		default_values.put(Boolean.class, false);
		default_values.put(String.class, "");
		default_values.put(Long.class, 0L);
	}

	public pascal_type get_primative_type(String s) {
		s = s.intern();
		if (s == "integer") {
			return INTEGER;
		}
		if (s == "double") {
			return DOUBLE;
		}
		if (s == "string") {
			return STRING;
		}
		if (s == "boolean") {
			return BOOLEAN;
		}
		if (s == "long") {
			return LONG;
		}
		if (s == "single") {
			return SINGLE;
		}
		if (s == "char") {
			return CHAR;
		}
		return null;
	}

	public pascal_type(Object object) {
		if (object instanceof custom_type) {
			reference = ((custom_type) object).type;
			is_custom_type = true;
		} else {
			reference = object.getClass();
			is_custom_type = false;
		}
	}

	public pascal_type(Class type) {
		this.reference = type;
		is_custom_type = false;
	}

	public pascal_type(custom_type_declaration type) {
		this.reference = type;
		is_custom_type = true;
	}

	public Object get_new() {
		if (is_custom_type) {
			assert (reference instanceof custom_type_declaration);
			custom_type_declaration d = (custom_type_declaration) reference;
			return d.get_new();
		} else {
			assert (reference instanceof Class);
			Class c = (Class) reference;
			return default_values.get(c);
		}
	}

	public boolean is_primative_wrapper() { // compiler better inline these...
		return is_String() || is_numerical() || is_boolean();
	}

	public boolean is_String() {
		return this == STRING;
	}

	public boolean is_single() {
		return this == SINGLE;
	}

	public boolean is_numerical() {
		return Number.class.isAssignableFrom((Class<?>) reference);
	}

	public boolean is_boolean() {
		return this == BOOLEAN;
	}

	public boolean is_double() {
		return this == DOUBLE;
	}

	public boolean is_char() {
		return this == CHAR;
	}

	public boolean is_long() {
		return this == LONG;
	}

	public boolean is_integer() {
		return this == INTEGER;
	}

	public pascal_type get_GCF_type(pascal_type other) {
		if (is_String() || other.is_String()) {
			return STRING;
		}
		if (is_double() || other.is_double()) {
			return DOUBLE;
		}
		if (is_single() || other.is_single()) {
			return SINGLE;
		}
		if (is_long() || other.is_long()) {
			return LONG;
		}
		if (is_integer() || other.is_integer()) {
			return INTEGER;
		}
		if (is_boolean() || other.is_boolean()) {
			return BOOLEAN;
		}
		return null;
	}

	public boolean is_custom_type() {
		return is_custom_type;
	}

	public Class get_reference(){
		return (Class) reference;
	}
}
