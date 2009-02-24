package pascal_types;

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
	public Object initialize() {
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
}
