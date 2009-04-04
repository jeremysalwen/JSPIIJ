package pascal_types;

import serp.bytecode.Code;

public class class_pascal_type extends pascal_type {
	Class c;

	public class_pascal_type(Class name) {
		c = name;
	}

	@Override
	public boolean isarray() {
		return false;
	}

	@Override
	public boolean equals(Object obj) {
		return c.equals(obj);
	}

	@Override
	public int hashCode() {
		return c.hashCode();
	}

	@Override
	public Object initialize() { //TODO FIX THIS
		try {
			return c.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Class toclass() {
		return c;
	}

	public void get_default_value_on_stack(Code code) {
		Object result;
		if ((result = pascal_type.default_values.get(this)) != null) {
			code.constant().setValue(result);
		} else {
			try {
				code.anew().setType(c);
				code.invokespecial().setMethod(c.getConstructor());
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
			return;
		}
	}
}
