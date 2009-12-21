package edu.js.interpreter.preprocessed;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

import ncsa.tools.common.util.TypeUtils;

import edu.js.interpreter.pascal_types.array_type;
import edu.js.interpreter.pascal_types.class_pascal_type;
import edu.js.interpreter.pascal_types.pascal_type;
import edu.js.interpreter.preprocessed.interpreting_objects.pointer;
import edu.js.interpreter.processing.pascal_program;

public class plugin_declaration extends abstract_function {
	Object owner;

	Method method;

	public plugin_declaration(Object owner, Method m) {
		this.owner = owner;
		method = m;
	}

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
	public pascal_type[] get_arg_types() {
		Class[] types = method.getParameterTypes();
		pascal_type[] result = new pascal_type[types.length];
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
				array_type type=new array_type(types[i])
			} else {
				result[i] = class_pascal_type
						.anew(types[i].isPrimitive() ? TypeUtils
								.getClassForType(types[i]) : types[i]);
			}
		}
		return result;
	}

pascal_type wrap_to_array_type(Class c) {
	if(!c.isArray()) {
		return class_pascal_type.anew(c);
	}
	pascal_type elem_type=c.getComponentType();
	return new array_type(elem_type,)
}
	@Override
	public String get_name() {
		return method.getName();
	}

	@Override
	public pascal_type get_return_type() {
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
