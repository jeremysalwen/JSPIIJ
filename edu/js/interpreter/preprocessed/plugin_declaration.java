package edu.js.interpreter.preprocessed;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

import ncsa.tools.common.util.TypeUtils;

import edu.js.interpreter.pascal_types.class_pascal_type;
import edu.js.interpreter.pascal_types.pascal_type;
import edu.js.interpreter.preprocessed.interpreting_objects.pointer;
import edu.js.interpreter.processing.pascal_program;

public class plugin_declaration extends abstract_function {
	Object owner;

	Method method;

	public plugin_declaration(Object owner, Method m) {
		method = m;
		this.owner = owner;
	}

	public Object call(pascal_program program, Object[] arguments) {
		try {
			return method.invoke(owner, arguments);
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
	public pascal_type[] get_arg_types() {
		Class[] types = method.getParameterTypes();
		pascal_type[] result = new pascal_type[types.length];
		for (int i = 0; i < types.length; i++) {
			if (types[i] == pointer.class) {
				types[i] = (Class) ((ParameterizedType) method
						.getGenericParameterTypes()[i])
						.getActualTypeArguments()[0];
			}
			result[i] = class_pascal_type
					.anew(types[i].isPrimitive() ? TypeUtils
							.getClassForType(types[i]) : types[i]);
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
			return class_pascal_type.anew((Class) ((ParameterizedType) method
					.getGenericReturnType()).getActualTypeArguments()[0]);
		}
		return class_pascal_type.anew(result);
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
