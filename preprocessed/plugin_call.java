package preprocessed;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;

import pascalTypes.pascalType;
import processing.pascalPlugin;

public class plugin_call extends returns_value {
	String name;
	LinkedList<returns_value> arguments;

	public plugin_call(String name, LinkedList<returns_value> arguments) {
		this.name = name;
		this.arguments = arguments;
	}

	@Override
	public pascalType get_value(function_on_stack f) {
		ArrayList<pascalType> pascal_args = new ArrayList<pascalType>(arguments
				.size());
		for (returns_value r : arguments) {
			pascal_args.add(r.get_value(f));
		}
		Class<pascalPlugin<pascalType>> plugin_class = f.program.plugins
				.get(name);
		try {
			Object o = plugin_class.getDeclaredConstructor(ArrayList.class)
					.newInstance(pascal_args);
			Method m = plugin_class.getMethod("process");
			return (pascalType) m.invoke(o, new Object[0]);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error invoking plugin call");
			return null;
		}
	}

}
