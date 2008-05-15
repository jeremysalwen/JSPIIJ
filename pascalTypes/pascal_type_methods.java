package pascalTypes;

import java.util.HashMap;

public class pascal_type_methods {
	static HashMap<Class, Object> default_values;
	static {
		default_values = new HashMap<Class, Object>();
		default_values.put(Integer.class, 0);
		default_values.put(Double.class, 0.0D);
		default_values.put(Boolean.class, false);
		default_values.put(String.class, "");
		default_values.put(Long.class, 0L);
	}

	public static Object get_default_value(Class c) {
		return default_values.get(c);
	}

	public static boolean is_primative_wrapper(Object o) {
		return (o instanceof Double || o instanceof Integer
				|| o instanceof String || o instanceof Boolean || o instanceof Long);
	}

	public static Class get_java_type(String name) {
		name = name.intern();
		if (name == "integer") {
			return Integer.class;
		}
		if (name == "string") {
			return String.class;
		}
		if (name == "float") {
			return Double.class;
		}
		if (name == "boolean") {
			return Boolean.class;
		}
		return null;
	}
}
