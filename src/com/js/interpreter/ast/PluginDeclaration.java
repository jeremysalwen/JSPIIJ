package com.js.interpreter.ast;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ncsa.tools.common.util.TypeUtils;

import com.js.interpreter.pascaltypes.ArgumentType;
import com.js.interpreter.pascaltypes.ArrayType;
import com.js.interpreter.pascaltypes.DeclaredType;
import com.js.interpreter.pascaltypes.JavaClassBasedType;
import com.js.interpreter.pascaltypes.RuntimeType;
import com.js.interpreter.pascaltypes.SubrangeType;
import com.js.interpreter.pascaltypes.VarargsType;
import com.js.interpreter.plugins.annotations.ArrayBoundsInfo;
import com.js.interpreter.plugins.annotations.MethodTypeData;
import com.js.interpreter.runtime.VariableBoxer;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;

public class PluginDeclaration extends AbstractFunction {
	Object owner;

	Method method;

	public PluginDeclaration(Object owner, Method m) {
		this.owner = owner;
		method = m;
	}

	@Override
	public Object call(VariableContext parentcontext,
			RuntimeExecutable<?> main, Object[] arguments) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
			return method.invoke(owner, arguments);
	}

	Type getFirstGenericType(Type t) {
		if (!(t instanceof ParameterizedType)) {
			return Object.class;
		}
		ParameterizedType param = (ParameterizedType) t;
		Type[] parameters = param.getActualTypeArguments();
		if (parameters.length == 0) {
			return Object.class;
		}
		return parameters[0];
	}

	DeclaredType convertBasicType(Type javatype) {
		Class<?> type = (Class<?>) javatype;
		return JavaClassBasedType.anew(type.isPrimitive() ? TypeUtils
				.getClassForType(type) : type);
	}

	DeclaredType convertArrayType(Type javatype,
			Iterator<SubrangeType> arraysizes) {
		Type subtype;
		SubrangeType arrayinfo;
		if (javatype instanceof GenericArrayType) {
			subtype = ((GenericArrayType) javatype).getGenericComponentType();
			arrayinfo = new SubrangeType();
		} else if (javatype instanceof Class<?>
				&& ((Class<?>) javatype).isArray()) {
			subtype = ((Class<?>) javatype).getComponentType();
			arrayinfo = new SubrangeType();
		} else {
			subtype = Object.class;
			arrayinfo = null;
		}

		if (arraysizes.hasNext()) {
			arrayinfo = arraysizes.next();
		}
		if (arrayinfo == null) {
			return convertBasicType(javatype);
		} else {
			return new ArrayType<DeclaredType>(convertArrayType(subtype,
					arraysizes), arrayinfo);
		}
	}

	RuntimeType convertReferenceType(Type javatype,
			Iterator<SubrangeType> arraysizes) {
		Type subtype = javatype;
		boolean pointer = javatype == VariableBoxer.class
				|| (javatype instanceof ParameterizedType && ((ParameterizedType) javatype)
						.getRawType() == VariableBoxer.class);
		if (pointer) {
			subtype = getFirstGenericType(javatype);
		}
		DeclaredType arraytype = convertArrayType(subtype, arraysizes);
		return new RuntimeType(arraytype, pointer);
	}

	RuntimeType deducePascalTypeFromJavaTypeAndAnnotations(Type javatype,
			ArrayBoundsInfo annotation) {

		List<SubrangeType> arrayinfo = new ArrayList<SubrangeType>();
		if (annotation != null && annotation.starts().length > 0) {
			int[] starts = annotation.starts();
			int[] lengths = annotation.lengths();
			for (int i = 0; i < starts.length; i++) {
				arrayinfo.add(new SubrangeType(starts[i], lengths[i]));
			}
		}
		Iterator<SubrangeType> iterator = arrayinfo.iterator();

		return convertReferenceType(javatype, iterator);
	}

	@Override
	public ArgumentType[] argumentTypes() {
		Type[] types = method.getGenericParameterTypes();
		ArgumentType[] result = new ArgumentType[types.length];
		MethodTypeData tmp = method.getAnnotation(MethodTypeData.class);
		ArrayBoundsInfo[] type_data = tmp == null ? null : tmp.info();
		for (int i = 0; i < types.length; i++) {
			RuntimeType argtype = deducePascalTypeFromJavaTypeAndAnnotations(
					types[i], type_data == null ? null : type_data[i]);
			if (i == types.length - 1 && method.isVarArgs()) {
				ArrayType<?> lastArgType = (ArrayType<?>) argtype.declType;
				result[i] = new VarargsType(new RuntimeType(
						lastArgType.element_type, argtype.writable));
			} else {
				result[i] = argtype;
			}
		}
		return result;
	}

	@Override
	public String name() {
		return method.getName();
	}

	@Override
	public DeclaredType return_type() {
		Class<?> result = method.getReturnType();
		if (result == VariableBoxer.class) {
			result = (Class<?>) ((ParameterizedType) method
					.getGenericReturnType()).getActualTypeArguments()[0];
		}
		if (result.isPrimitive()) {
			result = TypeUtils.getClassForType(result);
		}
		return JavaClassBasedType.anew(result);
	}

	@Override
	public boolean isByReference(int i) {
		return method.getParameterTypes()[i] == VariableBoxer.class;
	}

}
