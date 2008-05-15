package pascal_types;

public class var_args<T> {
	public T val;

	public var_args(T t) {
		this.val = t;
	}

	public T get() {
		return val;
	}

	public void set(T t) {
		this.val = t;
	}
}
