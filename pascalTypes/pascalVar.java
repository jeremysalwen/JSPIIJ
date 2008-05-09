package pascalTypes;

public class pascalVar<T> extends pascalType<T> {
	public void set(T value) {
		this.value=value;
	}

	public pascalVar(T value) {
		super(value);
	}

	public pascalVar(pascalType<T> value) {
		super(value);
	}
	public pascalVar(Class<T> type) {
		super(type);
	}
}
