package preprocessed.instructions.returns_value;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;

import preprocessed.interpreting_objects.function_on_stack;
import processing.pascalPlugin;

public class plugin_call extends returns_value {
	String name;
	LinkedList<returns_value> arguments;

	public plugin_call(String name, LinkedList<returns_value> arguments) {
		this.name = name;
		this.arguments = arguments;
	}

	@Override
	public Object get_value(function_on_stack f) {
		LinkedList<Object> pascal_args = new LinkedList<Object>();
		for (returns_value r : arguments) {
			pascal_args.add(r.get_value(f));
		}
		Class<pascalPlugin> plugin_class = f.program.plugins
				.get(name);
		try {
			Object o = plugin_class.getDeclaredConstructor(ArrayList.class)
					.newInstance(pascal_args);
			Method m = plugin_class.getMethod("process");
			return m.invoke(o, new Object[0]);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error invoking plugin call");
			return null;
		}
	}

}
