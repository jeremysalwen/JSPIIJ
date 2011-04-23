package com.js.interpreter.pascaltypes;

import java.util.HashMap;

import ncsa.tools.common.util.TypeUtils;
import serp.bytecode.Code;

import com.js.interpreter.ast.CompileTimeContext;
import com.js.interpreter.ast.ExpressionContext;
import com.js.interpreter.ast.instructions.SetValueExecutable;
import com.js.interpreter.ast.instructions.returnsvalue.ReturnsValue;
import com.js.interpreter.ast.instructions.returnsvalue.boxing.CharacterBoxer;
import com.js.interpreter.ast.instructions.returnsvalue.boxing.StringBoxer;
import com.js.interpreter.ast.instructions.returnsvalue.boxing.StringBuilderBoxer;
import com.js.interpreter.exceptions.ParsingException;
import com.js.interpreter.exceptions.UnassignableTypeException;
import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.pascaltypes.typeconversion.TypeConverter;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public class JavaClassBasedType extends DeclaredType {
	Class c;

	protected static HashMap<DeclaredType, Object> default_values = new HashMap<DeclaredType, Object>();

	public static DeclaredType Boolean = new JavaClassBasedType(Boolean.class);

	public static DeclaredType Character = new JavaClassBasedType(
			Character.class);

	public static DeclaredType StringBuilder = new JavaClassBasedType(
			StringBuilder.class);

	public static DeclaredType Long = new JavaClassBasedType(Long.class);

	public static DeclaredType Double = new JavaClassBasedType(Double.class);

	public static DeclaredType Integer = new JavaClassBasedType(Integer.class);

	static {
		default_values.put(JavaClassBasedType.Integer, 0);
		default_values.put(JavaClassBasedType.StringBuilder, "");
		default_values.put(JavaClassBasedType.Double, 0.0D);
		default_values.put(JavaClassBasedType.Long, 0L);
		default_values.put(JavaClassBasedType.Character, '\0');
		default_values.put(JavaClassBasedType.Boolean, false);
	}

	private JavaClassBasedType(Class name) {
		c = name;
	}

	@Override
	public boolean isarray() {
		return false;
	}

	@Override
	public boolean equals(DeclaredType obj) {
		if (this == obj) {
			return true;
		}
		if (obj instanceof JavaClassBasedType) {
			Class other = ((JavaClassBasedType) obj).c;
			return c == other || c == Object.class || other == Object.class;
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
	public String toString() {
		if (this == Boolean) {
			return "Boolean";
		} else if (this == Character) {
			return "Character";
		} else if (this == Integer) {
			return "Integer";
		} else if (this == Double) {
			return "Double";
		} else if (this == Long) {
			return "Long";
		} else if (this == StringBuilder) {
			return "String";
		}
		return c.getCanonicalName();
	}

	public static DeclaredType anew(Class c) {
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
	public ReturnsValue convert(ReturnsValue value, ExpressionContext f)
			throws ParsingException {

		RuntimeType other_type = value.get_type(f);

		if (other_type.declType instanceof JavaClassBasedType) {

			if (this.equals(other_type.declType)) {
				return cloneValue(value);
			}
			if (this == StringBuilder
					&& other_type.declType == JavaClassBasedType.Character) {
				return new CharacterBoxer(value);
			}
			if (this == StringBuilder
					&& ((JavaClassBasedType) other_type.declType).c == String.class) {
				return new StringBoxer(value);
			}
			if (this.c == String.class
					&& other_type.declType == JavaClassBasedType.StringBuilder) {
				return new StringBuilderBoxer(value);
			}
			if (this.c == String.class
					&& other_type.declType == JavaClassBasedType.Character) {
				return new StringBuilderBoxer(new CharacterBoxer(value));
			}
			return TypeConverter.autoConvert(this, value,
					(JavaClassBasedType) other_type.declType);
		}
		return null;
	}

	@Override
	public void pushDefaultValue(Code constructor_code) {
		Object value = default_values.get(this);
		if (value != null) {
			constructor_code.constant().setValue(value);
		}
	}

	@Override
	public ReturnsValue cloneValue(final ReturnsValue r) {
		if (this == StringBuilder) {
			return new ReturnsValue() {

				@Override
				public RuntimeType get_type(ExpressionContext f)
						throws ParsingException {
					return r.get_type(f);
				}

				@Override
				public Object getValue(VariableContext f,
						RuntimeExecutable<?> main)
						throws RuntimePascalException {
					StringBuilder other = (StringBuilder) r.getValue(f, main);
					return new StringBuilder(other);
				}

				@Override
				public LineInfo getLineNumber() {
					return r.getLineNumber();
				}

				@Override
				public SetValueExecutable createSetValueInstruction(
						ReturnsValue r) throws UnassignableTypeException {
					throw new UnassignableTypeException(this);
				}

				@Override
				public Object compileTimeValue(CompileTimeContext context)
						throws ParsingException {
					Object val = r.compileTimeValue(context);
					if (val != null) {
						return new java.lang.StringBuilder((StringBuilder) val);
					}
					return null;
				}
			};
		} else {
			return r;
		}
	}
}
