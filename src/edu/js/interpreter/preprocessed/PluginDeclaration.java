package edu.js.interpreter.preprocessed;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

import ncsa.tools.common.util.TypeUtils;

import edu.js.interpreter.pascaltypes.ArrayType;
import edu.js.interpreter.pascaltypes.JavaClassBasedType;
import edu.js.interpreter.pascaltypes.PascalType;
import edu.js.interpreter.plugins.annotations.ArrayBoundsInfo;
import edu.js.interpreter.plugins.annotations.MethodTypeData;
import edu.js.interpreter.preprocessed.interpretingobjects.Pointer;
import edu.js.interpreter.processing.PascalProgram;

public class PluginDeclaration extends AbstractFunction {
	Object owner;

	Method method;

	public PluginDeclaration(Object owner, Method m) {
		this.owner = owner;
		method = m;
	}

	@Override
	public Object call(PascalProgram program, Object[] arguments) {
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
	public PascalType[] argument_types() {
		Class[] types = method.getParameterTypes();
		PascalType[] result = new PascalType[types.length];
		MethodTypeData tmp = method.getAnnotation(MethodTypeData.class);
		ArrayBoundsInfo[] type_data = tmp == null ? null : tmp.info();
		for (int i = 0; i < types.length; i++) {
			if (types[i] == String.class) {
				types[i] = StringBuilder.class;
			}
			if (types[i] == Pointer.class) {
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
				result[i] = new ArrayType(JavaClassBasedType.anew(types[i]
						.getComponentType()), starts, lengths);
			} else {
				result[i] = JavaClassBasedType
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
	public PascalType return_type() {
		Class result = method.getReturnType();
		if (result == Pointer.class) {
			result = (Class) ((ParameterizedType) method.getGenericReturnType())
					.getActualTypeArguments()[0];
		}
		if (result.isPrimitive()) {
			result = TypeUtils.getClassForType(result);
		}
		return JavaClassBasedType.anew(result);
	}

	@Override
	public boolean is_varargs(int i) {
		return method.getParameterTypes()[i] == Pointer.class;
	}

}
