package pascal_types;

import java.lang.reflect.Array;

import serp.bytecode.Code;

public class array_type extends pascal_type {
	public pascal_type element_type;

	public int lower_bound;

	public int upper_bound;

	public array_type(pascal_type element_class, int lower, int upper) {
		this.element_type = element_class;
		this.lower_bound = lower;
		this.upper_bound = upper;
	}

	@Override
	public boolean isarray() {
		return true;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof array_type) {
			array_type o = (array_type) obj;
			return o.element_type.equals(element_type)
					&& o.lower_bound == lower_bound
					&& o.upper_bound == upper_bound;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return (element_type.hashCode() * 31 + lower_bound) * 31 + upper_bound;
	}

	@Override
	public Object initialize() {
		return Array.newInstance(element_type.toclass(), upper_bound
				- lower_bound + 1);
	}

	@Override
	public Class toclass() {
		try {
			return Class.forName("L"
					+ element_type.toclass().getCanonicalName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void get_default_value_on_stack(Code code) {
		int i = 1;
		pascal_type sub_type = element_type;
		while (true) {
			if (sub_type.isarray()) {
				i++;
				array_type next = sub_type.get_type_array();
				code.constant().setValue(
						next.upper_bound - next.lower_bound + 1);
				sub_type = next.element_type;
			} else {
				break;
			}
		}
		code.multianewarray().setDimensions(i).setType(sub_type.toclass());

	}
}
