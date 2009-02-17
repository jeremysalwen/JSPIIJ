package preprocessed.interpreting_objects;

import java.lang.reflect.Array;

public class arraypointer extends pointer {
	Object array;

	int index;

	public arraypointer(Object var_holder, int i) {
		array = var_holder;
		index = i;
	}

	@Override
	public Object get() {
		return Array.get(array, index);
	}

	@Override
	public void set(Object value) {
		Array.set(array, index, value);

	}

}
