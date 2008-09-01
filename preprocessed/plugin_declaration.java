package preprocessed;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import processing.pascal_program;

public class plugin_declaration extends abstract_function {
	Method method;

	public plugin_declaration(Method m) {
		method = m;
	}

	public Object call(pascal_program program, Object[] arguments) {
		try {
			System.out.println(arguments);
			return method.invoke(null, arguments);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	Class[] get_arg_types() {
		return method.getParameterTypes();
	}

	@Override
	String get_name() {
		return method.getName();
	}

	@Override
	public Class get_return_type() {
		return method.getReturnType();
	}

}
