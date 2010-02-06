package edu.js.interpreter.pascaltypes;

import java.util.HashMap;

import edu.js.interpreter.preprocessed.FunctionDeclaration;
import edu.js.interpreter.preprocessed.instructions.returnsvalue.ReturnsValue;

import ncsa.tools.common.util.TypeUtils;

import serp.bytecode.Code;

public class JavaClassBasedType extends PascalType {
	Class c;

	protected static HashMap<PascalType, Object> default_values = new HashMap<PascalType, Object>();

	public static PascalType Boolean = new JavaClassBasedType(Boolean.class);

	public static PascalType Character = new JavaClassBasedType(Character.class);

	public static PascalType StringBuilder = new JavaClassBasedType(
			StringBuilder.class);

	public static PascalType Long = new JavaClassBasedType(Long.class);

	public static PascalType Double = new JavaClassBasedType(Double.class);

	public static PascalType Integer = new JavaClassBasedType(Integer.class);
	static {
		default_values.put(JavaClassBasedType.Integer, 0);
		default_values.put(JavaClassBasedType.StringBuilder, "");
		default_values.put(JavaClassBasedType.Double, 0.0D);
		default_values.put(JavaClassBasedType.Long, 0L);
		default_values.put(JavaClassBasedType.Character, '\0');
	}

	private JavaClassBasedType(Class name) {
		c = name;
	}

	@Override
	public boolean isarray() {
		return false;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj instanceof JavaClassBasedType) {
			return c.equals(((JavaClassBasedType) obj).c);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return (TypeUtils.isPrimitiveWrapper(c) ? TypeUtils.getTypeForClass(c)
				: c).getCanonicalName().hashCode();

	}

	@Override
	public Object initialize() {
		Object result;
		if ((result = JavaClassBasedType.default_values.get(this)) != null) {
			return result;
		} else {
			try {
				return c.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}

			return null;
		}
	}

	@Override
	public Class toclass() {
		return c;
	}

	@Override
	public void get_default_value_on_stack(Code code) {
		Object result;
		if ((result = JavaClassBasedType.default_values.get(this)) != null) {
			code.constant().setValue(result);
		} else {
			try {
				code.anew().setType(c);
				code.invokespecial().setMethod(c.getConstructor());
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
			return;
		}
	}

	@Override
	public String toString() {
		return "class_pascal_type :" + c.toString();
	}

	public static PascalType anew(Class c) {
		if (c == Integer.class) {
			return JavaClassBasedType.Integer;
		}
		if (c == Double.class) {
			return JavaClassBasedType.Double;
		}
		if (c == StringBuilder.class) {
			return JavaClassBasedType.StringBuilder;
		}
		if (c == Long.class) {
			return JavaClassBasedType.Long;
		}
		if (c == Character.class) {
			return JavaClassBasedType.Character;
		}
		if (c == Boolean.class) {
			return JavaClassBasedType.Boolean;
		}
		return new JavaClassBasedType(c);
	}

	@Override
	public ReturnsValue convert(ReturnsValue value, FunctionDeclaration f) {
		PascalType other_type = value.get_type(f);
		if (this.equals(other_type)) {
			return value;
		}
		if(other_type instanceof ReferenceType) {
			ReferenceType other=(ReferenceType)other_type;
			if(this.equals(other.child_type)) {
				return value;
			}
		}
		/*
		 * TODO: Add some conversions here.
		 */
		return null;
	}

}
