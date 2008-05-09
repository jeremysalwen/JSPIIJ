package processing;

import java.lang.reflect.Field;
import java.util.ArrayList;

import pascalTypes.pascalType;

public abstract class pascalPlugin<T> {
	public abstract T process();

	@SuppressWarnings("unchecked")
	public pascalPlugin(ArrayList<pascalType> arrayOfArgs) throws Exception {
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
		for (int i = 0; i < arrayOfArgs.size(); i++) {
			fields[i].set(this, arrayOfArgs.get(i).get());
		}
	}

}
