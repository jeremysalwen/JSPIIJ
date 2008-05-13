package pascalTypes;

import java.util.HashMap;

public class standard_type<T> implements pascal_type {
	static HashMap<Class, Object> default_values;
	static {
		default_values = new HashMap<Class, Object>();
		default_values.put(Integer.class, 0);
		default_values.put(Double.class, 0.0D);
		default_values.put(Boolean.class, false);
		default_values.put(String.class, "");
	}
	protected T value;

	public T get() {
		return value;
	}

	public standard_type(T value) {
		this.value = value;
	}

	public standard_type(standard_type<T> value) {
		this.value = value.get();
	}

	public final boolean is_variable() {
		return this instanceof standard_var;
	}

	public standard_type(Class<T> type) {
		this((T) default_values.get(type));
	}

	public int getType() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@SuppressWarnings("unchecked")
	static public Class get_java_type(String pascal_name) {
		return null;  //TODO
	}
}
