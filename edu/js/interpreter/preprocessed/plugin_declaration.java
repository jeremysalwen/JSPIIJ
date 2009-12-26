package edu.js.interpreter.preprocessed;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

import ncsa.tools.common.util.TypeUtils;

import edu.js.interpreter.pascal_types.array_type;
import edu.js.interpreter.pascal_types.class_pascal_type;
import edu.js.interpreter.pascal_types.pascal_type;
import edu.js.interpreter.plugins.annotations.array_bounds_info;
import edu.js.interpreter.plugins.annotations.method_type_data;
import edu.js.interpreter.preprocessed.interpreting_objects.pointer;
import edu.js.interpreter.processing.pascal_program;

public class plugin_declaration extends abstract_function {
	Object owner;

	Method method;

	public plugin_declaration(Object owner, Method m) {
		this.owner = owner;
		method = m;
	}

	@Override
	public Object call(pascal_program program, Object[] arguments) {
		try {
			for (int i = 0; i < arguments.length; i++) {
				if (arguments[i] instanceof StringBuilder) {
					arguments[i] = arguments[i].toString();
				}
			}
			return method.invoke(owner, arguments);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	@Override
	public pascal_type[] argument_types() {
		Class[] types = method.getParameterTypes();
		pascal_type[] result = new pascal_type[types.length];
		method_type_data tmp = method.getAnnotation(method_type_data.class);
		array_bounds_info[] type_data = tmp == null ? null : tmp.info();
		for (int i = 0; i < types.length; i++) {
			if (types[i] == String.class) {
				types[i] = StringBuilder.class;
			}
			if (types[i] == pointer.class) {
				types[i] = (Class) ((ParameterizedType) method
						.getGenericParameterTypes()[i])
						.getActualTypeArguments()[0];
			}
			if (types[i].isArray()) {
				int[] starts = new int[] { 0 };
				int[] lengths = new int[] { 0 };
				if (type_data != null) {
					starts = type_data[i].starts();
					lengths = type_data[i].lengths();
				}
				result[i] = new array_type(class_pascal_type.anew(types[i]
						.getComponentType()), starts, lengths);
			} else {
				result[i] = class_pascal_type
						.anew(types[i].isPrimitive() ? TypeUtils
								.getClassForType(types[i]) : types[i]);
			}
		}
		return result;
	}

	@Override
	public String name() {
		return method.getName();
	}

	
	@Override
	public pascal_type return_type() {
		Class result = method.getReturnType();
		if (result == pointer.class) {
			result = (Class) ((ParameterizedType) method.getGenericReturnType())
					.getActualTypeArguments()[0];
		}
		if (result.isPrimitive()) {
			result = TypeUtils.getClassForType(result);
		}
		return class_pascal_type.anew(result);
	}

	@Override
	public boolean is_varargs(int i) {
		return method.getParameterTypes()[i] == pointer.class;
	}

}
