package pascal_types;

import java.util.HashMap;

public class pascal_type {
	final Object reference;
	boolean is_custom_type;

	static HashMap<Class, Object> default_values;
	static {
		default_values = new HashMap<Class, Object>();
		default_values.put(Integer.class, 0);
		default_values.put(Double.class, 0.0D);
		default_values.put(Boolean.class, false);
		default_values.put(String.class, "");
		default_values.put(Long.class, 0L);
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

	public boolean is_primative_wrapper() {
		return (reference instanceof Double || reference instanceof Integer
				|| reference instanceof String || reference instanceof Boolean || reference instanceof Long);
	}

	public boolean is_String() {
		return reference == String.class;
	}

	public boolean is_floating_point() {
		return reference == Float.class || reference == Double.class;
	}

	public boolean is_numerical() {
		return reference instanceof Number;
	}

	public boolean is_boolean() {
		return reference instanceof Boolean;
	}
}
