package pascalTypes;

import java.util.HashMap;

public class pascalType<T> {
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

	public pascalType(T value) {
		this.value = value;
	}

	public pascalType(pascalType<T> value) {
		this.value = value.get();
	}

	public final boolean is_variable() {
		return this instanceof pascalVar;
	}

	public pascalType(Class<T> type) {
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
