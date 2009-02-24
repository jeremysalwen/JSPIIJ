package preprocessed;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

import pascal_types.class_pascal_type;
import pascal_types.pascal_type;
import preprocessed.interpreting_objects.pointer;
import processing.pascal_program;

public class plugin_declaration extends abstract_function {
	Method method;

	public plugin_declaration(Method m) {
		method = m;
	}

	public Object call(pascal_program program, Object[] arguments) {
		try {
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

	Class box(Class c) {
		if (c == int.class) {
			return Integer.class;
		}
		if (c == double.class) {
			return Double.class;
		}
		if (c == char.class) {
			return Character.class;
		}
		if (c == float.class) {
			return Float.class;
		}
		return c;
	}

	@Override
	public pascal_type[] get_arg_types() {
		Class[] types = method.getParameterTypes();
		pascal_type[] result = new pascal_type[types.length];
		for (int i = 0; i < types.length; i++) {
			if (types[i] == pointer.class) {
				types[i] = (Class) ((ParameterizedType) method
						.getGenericParameterTypes()[i])
						.getActualTypeArguments()[0];
			}
			result[i] = new class_pascal_type(box(types[i]));
		}
		return result;
	}

	@Override
	public String get_name() {
		return method.getName();
	}

	@Override
	public pascal_type get_return_type() {
		Class result = method.getReturnType();
		if (result == pointer.class) {
			return new class_pascal_type((Class) ((ParameterizedType) method
					.getGenericReturnType()).getActualTypeArguments()[0]);
		}
		return new class_pascal_type(result);
	}

	@Override
	public boolean is_varargs(int i) {
		return method.getParameterTypes()[i] == pointer.class;
	}

	@Override
	public String toString() {
		return method.toString();
	}
}
