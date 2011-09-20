package com.js.interpreter.pascaltypes.typeconversion;

import java.util.HashMap;

import com.js.interpreter.ast.CompileTimeContext;
import com.js.interpreter.ast.ExpressionContext;
import com.js.interpreter.ast.instructions.SetValueExecutable;
import com.js.interpreter.ast.returnsvalue.ReturnsValue;
import com.js.interpreter.exceptions.UnconvertibleTypeException;
import com.js.interpreter.exceptions.ParsingException;
import com.js.interpreter.exceptions.UnassignableTypeException;
import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.pascaltypes.JavaClassBasedType;
import com.js.interpreter.pascaltypes.RuntimeType;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public class TypeConverter {
	static HashMap<Class, Integer> precedence = new HashMap<Class, Integer>();
	static {
		precedence.put(Character.class, 0);
		precedence.put(Integer.class, 1);
		precedence.put(Long.class, 2);
		precedence.put(Double.class, 3);
	}

	public static ReturnsValue autoConvert(JavaClassBasedType outtype,
			ReturnsValue target, JavaClassBasedType intype) {
		if (intype == outtype) {
			return target;
		}
		Integer inprecedence = precedence.get(intype.getTransferClass());
		Integer outprecedence = precedence.get(outtype.getTransferClass());
		if (inprecedence != null && outprecedence != null) {
			if (inprecedence < outprecedence) {
				return forceConvert(outtype, target, intype);
			}
		}
		if(outtype==JavaClassBasedType.StringBuilder && intype == JavaClassBasedType.Character){
			return forceConvert(outtype, target, intype);
		}
		return null;
	}

	public static ReturnsValue autoConvertRequired(JavaClassBasedType outtype,
			ReturnsValue target, JavaClassBasedType intype)
			throws UnconvertibleTypeException {
		ReturnsValue result = autoConvert(outtype, target, intype);
		if (result == null) {
			throw new UnconvertibleTypeException(target, outtype, intype, true);
		}
		return result;
	}

	public static ReturnsValue forceConvertRequired(JavaClassBasedType outtype,
			ReturnsValue target, JavaClassBasedType intype)
			throws UnconvertibleTypeException {
		ReturnsValue result = forceConvert(outtype, target, intype);
		if (result == null) {
			throw new UnconvertibleTypeException(target, outtype, intype, false);
		}
		return result;
	}

	public static ReturnsValue forceConvert(JavaClassBasedType outtype,
			ReturnsValue target, JavaClassBasedType intype) {
		if (outtype == intype) {
			return target;
		}
		if(outtype.equals(JavaClassBasedType.StringBuilder)) {
			return new AnyToString(target);
		}
		if (intype == JavaClassBasedType.Character) {
			target = new CharToInt(target);
			if (outtype == JavaClassBasedType.Integer) {
				return target;
			} else if (outtype == JavaClassBasedType.Long) {
				return new NumberToLong(target);
			} else if (outtype == JavaClassBasedType.Double) {
				return new NumberToReal(target);
			}
		}
		if (intype == JavaClassBasedType.Integer) {
			if (outtype == JavaClassBasedType.Character) {
				return new NumberToChar(target);
			} else if (outtype == JavaClassBasedType.Long) {
				return new NumberToLong(target);
			} else if (outtype == JavaClassBasedType.Double) {
				return new NumberToReal(target);
			}
		}
		if (intype == JavaClassBasedType.Long) {
			if (outtype == JavaClassBasedType.Character) {
				return new NumberToChar(target);
			} else if (outtype == JavaClassBasedType.Integer) {
				return new NumberToInt(target);
			} else if (outtype == JavaClassBasedType.Double) {
				return new NumberToReal(target);
			}
		}
		if (intype == JavaClassBasedType.Double) {
			if (outtype == JavaClassBasedType.Character) {
				return new NumberToChar(target);
			} else if (outtype == JavaClassBasedType.Integer) {
				return new NumberToInt(target);
			} else if (outtype == JavaClassBasedType.Long) {
				return new NumberToLong(target);
			}
		}
		return null;
	}

	static class NumberToReal implements ReturnsValue {
		ReturnsValue other;

		public NumberToReal(ReturnsValue other) {
			this.other = other;
		}

		@Override
		public Object getValue(VariableContext f, RuntimeExecutable<?> main)
				throws RuntimePascalException {
			Number i = (Number) other.getValue(f, main);
			return i.doubleValue();
		}

		@Override
		public RuntimeType get_type(ExpressionContext f)
				throws ParsingException {
			return new RuntimeType(JavaClassBasedType.Double, false);
		}

		@Override
		public LineInfo getLineNumber() {
			return other.getLineNumber();
		}

		@Override
		public Object compileTimeValue(CompileTimeContext context)
				throws ParsingException {
			Object o = other.compileTimeValue(context);
			if (o != null) {
				return ((Number) o).doubleValue();
			} else {
				return null;
			}
		}

		@Override
		public SetValueExecutable createSetValueInstruction(ReturnsValue r)
				throws UnassignableTypeException {
			throw new UnassignableTypeException(this);
		}

		@Override
		public ReturnsValue compileTimeExpressionFold(CompileTimeContext context)
				throws ParsingException {
			return new NumberToReal(other.compileTimeExpressionFold(context));
		}
	}

	static class NumberToLong implements ReturnsValue {
		ReturnsValue other;

		public NumberToLong(ReturnsValue other) {
			this.other = other;
		}

		@Override
		public Object getValue(VariableContext f, RuntimeExecutable<?> main)
				throws RuntimePascalException {
			Number i = (Number) other.getValue(f, main);
			return i.longValue();
		}

		@Override
		public RuntimeType get_type(ExpressionContext f)
				throws ParsingException {
			return new RuntimeType(JavaClassBasedType.Long, false);
		}

		@Override
		public LineInfo getLineNumber() {
			return other.getLineNumber();
		}

		@Override
		public Object compileTimeValue(CompileTimeContext context)
				throws ParsingException {
			Object o = other.compileTimeValue(context);
			if (o != null) {
				return ((Number) o).longValue();
			} else {
				return null;
			}
		}

		@Override
		public SetValueExecutable createSetValueInstruction(ReturnsValue r)
				throws UnassignableTypeException {
			throw new UnassignableTypeException(this);
		}

		@Override
		public ReturnsValue compileTimeExpressionFold(CompileTimeContext context)
				throws ParsingException {
			return new NumberToLong(other.compileTimeExpressionFold(context));
		}
	}

	static class NumberToChar implements ReturnsValue {
		ReturnsValue other;

		public NumberToChar(ReturnsValue other) {
			this.other = other;
		}

		@Override
		public Object getValue(VariableContext f, RuntimeExecutable<?> main)
				throws RuntimePascalException {
			Number i = (Number) other.getValue(f, main);
			return (char) i.longValue();
		}

		@Override
		public RuntimeType get_type(ExpressionContext f)
				throws ParsingException {
			return new RuntimeType(JavaClassBasedType.Character, false);
		}

		@Override
		public LineInfo getLineNumber() {
			return other.getLineNumber();
		}

		@Override
		public Object compileTimeValue(CompileTimeContext context)
				throws ParsingException {
			Object o = other.compileTimeValue(context);
			if (o != null) {
				return (char) ((Number) o).longValue();
			} else {
				return null;
			}
		}

		@Override
		public SetValueExecutable createSetValueInstruction(ReturnsValue r)
				throws UnassignableTypeException {
			throw new UnassignableTypeException(this);
		}

		@Override
		public ReturnsValue compileTimeExpressionFold(CompileTimeContext context)
				throws ParsingException {
			return new NumberToChar(other.compileTimeExpressionFold(context));
		}
	}

	static class NumberToInt implements ReturnsValue {
		ReturnsValue other;

		public NumberToInt(ReturnsValue other) {
			this.other = other;
		}

		@Override
		public Object getValue(VariableContext f, RuntimeExecutable<?> main)
				throws RuntimePascalException {
			Number i = (Number) other.getValue(f, main);
			return i.intValue();
		}

		@Override
		public RuntimeType get_type(ExpressionContext f)
				throws ParsingException {
			return new RuntimeType(JavaClassBasedType.Integer, false);
		}

		@Override
		public LineInfo getLineNumber() {
			return other.getLineNumber();
		}

		@Override
		public Object compileTimeValue(CompileTimeContext context)
				throws ParsingException {
			Object o = other.compileTimeValue(context);
			if (o != null) {
				return ((Number) o).intValue();
			} else {
				return null;
			}
		}

		@Override
		public SetValueExecutable createSetValueInstruction(ReturnsValue r)
				throws UnassignableTypeException {
			throw new UnassignableTypeException(this);
		}

		@Override
		public ReturnsValue compileTimeExpressionFold(CompileTimeContext context)
				throws ParsingException {
			return new NumberToInt(other.compileTimeExpressionFold(context));
		}
	}

	static class CharToInt implements ReturnsValue {
		ReturnsValue other;

		public CharToInt(ReturnsValue other) {
			this.other = other;
		}

		@Override
		public Object getValue(VariableContext f, RuntimeExecutable<?> main)
				throws RuntimePascalException {
			Character i = (Character) other.getValue(f, main);
			return (int) i.charValue();
		}

		@Override
		public RuntimeType get_type(ExpressionContext f)
				throws ParsingException {
			return new RuntimeType(JavaClassBasedType.Integer, false);
		}

		@Override
		public LineInfo getLineNumber() {
			return other.getLineNumber();
		}

		@Override
		public Object compileTimeValue(CompileTimeContext context)
				throws ParsingException {
			Object o = other.compileTimeValue(context);
			if (o != null) {
				return (int) ((Character) o).charValue();
			} else {
				return null;
			}
		}

		@Override
		public SetValueExecutable createSetValueInstruction(ReturnsValue r)
				throws UnassignableTypeException {
			throw new UnassignableTypeException(this);
		}

		@Override
		public ReturnsValue compileTimeExpressionFold(CompileTimeContext context)
				throws ParsingException {
			return new CharToInt(other.compileTimeExpressionFold(context));
		}
	}
	public static class AnyToString implements ReturnsValue {
		ReturnsValue other;

		public AnyToString(ReturnsValue other) {
			this.other = other;
		}

		@Override
		public Object getValue(VariableContext f, RuntimeExecutable<?> main)
				throws RuntimePascalException {
			return other.getValue(f, main).toString();
		}

		@Override
		public RuntimeType get_type(ExpressionContext f)
				throws ParsingException {
			return new RuntimeType(JavaClassBasedType.anew(String.class), false);
		}

		@Override
		public LineInfo getLineNumber() {
			return other.getLineNumber();
		}

		@Override
		public Object compileTimeValue(CompileTimeContext context)
				throws ParsingException {
			Object o = other.compileTimeValue(context);
			if (o != null) {
				return o.toString();
			} else {
				return null;
			}
		}

		@Override
		public SetValueExecutable createSetValueInstruction(ReturnsValue r)
				throws UnassignableTypeException {
			throw new UnassignableTypeException(this);
		}

		@Override
		public ReturnsValue compileTimeExpressionFold(CompileTimeContext context)
				throws ParsingException {
			return new AnyToString(other.compileTimeExpressionFold(context));
		}
	}
}
