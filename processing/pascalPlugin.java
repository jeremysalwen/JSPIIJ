package processing;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;

import pascalTypes.custom_type;
import pascalTypes.pascal_type;
import pascalTypes.standard_type;

public abstract class pascalPlugin<T> {
	public abstract T process();

	@SuppressWarnings("unchecked")
	public pascalPlugin(LinkedList<pascal_type> arrayOfArgs) throws Exception {
		Class callingClass = null;
		try {
			callingClass = Class.forName(new Throwable().getStackTrace()[1]
					.getClassName());
		} catch (ClassNotFoundException e) {
			throw new Exception("Could not find calling class", e);
		}
		Field[] fields = callingClass.getFields();
		// if (!StaticMethods.argsMatch(arrayOfArgs, callingClass))
		// throw new IllegalArgumentException("Args array does not match");
		ListIterator<pascal_type> i = arrayOfArgs.listIterator();
		for (pascal_type p = null; i.hasNext(); p = i.next()) {
			if (p instanceof standard_type) {
				standard_type t = (standard_type) p;
				if (t.get().getClass() == fields[i.previousIndex()].getType()) {
					fields[i.previousIndex()].set(this, ((standard_type) p)
							.get());
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
