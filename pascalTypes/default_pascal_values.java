package pascalTypes;

import java.util.HashMap;

public class default_pascal_values {
	static HashMap<Class, Object> default_values;
	static {
		default_values = new HashMap<Class, Object>();
		default_values.put(Integer.class, 0);
		default_values.put(Double.class, 0.0D);
		default_values.put(Boolean.class, false);
		default_values.put(String.class, "");
	}

	public static Object get_default_value(Class c) {
		return default_values.get(c);
	}
}
