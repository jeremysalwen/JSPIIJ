package processing;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.ListIterator;

import pascal_types.custom_type;
import pascal_types.pascal_type_methods;

public abstract class pascalPlugin<T> {
	public abstract T process();

	@SuppressWarnings("unchecked")
	public pascalPlugin(LinkedList<Object> arrayOfArgs) throws Exception {
		Class callingClass = null;
		try {
			callingClass = Class.forName(new Throwable().getStackTrace()[1]
					.getClassName());
		} catch (ClassNotFoundException e) {
			throw new Exception("Could not find calling class", e);
		}
		Field[] fields = callingClass.getFields();
		ListIterator<Object> i = arrayOfArgs.listIterator();
		while (i.hasNext()) {
			Object p = null;
			p = i.next();
			if (pascal_type_methods.is_primative_wrapper(p)) {
				if (p.getClass() == fields[i.previousIndex()].getType()) {
					fields[i.previousIndex()].set(this, p);
				} else {
					throw new IllegalArgumentException(
							"arguments to plugin call do not match");
				}
			} else if (p instanceof custom_type) {
				if (fields[i.previousIndex()].getType() == custom_type.class) {
					fields[i.previousIndex()].set(this, p);
				} else {
					throw new IllegalArgumentException(
							"arguments to plugin call do not match");
				}
			}
		}
	}
}
