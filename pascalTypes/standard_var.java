package pascalTypes;

public class standard_var<T> extends standard_type<T> {
	public void set(T value) {
		this.value=value;
	}

	public standard_var(T value) {
		super(value);
	}

	public standard_var(standard_type<T> value) {
		super(value);
	}
	public standard_var(Class<T> type) {
		super(type);
	}
}
