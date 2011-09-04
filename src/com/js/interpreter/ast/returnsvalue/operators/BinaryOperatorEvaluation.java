package com.js.interpreter.ast.returnsvalue.operators;

import javax.naming.OperationNotSupportedException;

import com.js.interpreter.ast.CompileTimeContext;
import com.js.interpreter.ast.ExpressionContext;
import com.js.interpreter.ast.instructions.SetValueExecutable;
import com.js.interpreter.ast.returnsvalue.ConstantAccess;
import com.js.interpreter.ast.returnsvalue.DebuggableReturnsValue;
import com.js.interpreter.ast.returnsvalue.ReturnsValue;
import com.js.interpreter.exceptions.BadOperationTypeException;
import com.js.interpreter.exceptions.ConstantCalculationException;
import com.js.interpreter.exceptions.ParsingException;
import com.js.interpreter.exceptions.UnassignableTypeException;
import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.pascaltypes.DeclaredType;
import com.js.interpreter.pascaltypes.JavaClassBasedType;
import com.js.interpreter.pascaltypes.RuntimeType;
import com.js.interpreter.pascaltypes.typeconversion.TypeConverter;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.PascalArithmeticException;
import com.js.interpreter.runtime.exception.RuntimePascalException;
import com.js.interpreter.runtime.exception.internal.InternalInterpreterException;
import com.js.interpreter.tokens.OperatorTypes;

public abstract class BinaryOperatorEvaluation extends DebuggableReturnsValue {
	OperatorTypes operator_type;

	ReturnsValue operon1;

	ReturnsValue operon2;
	LineInfo line;

	public BinaryOperatorEvaluation(ReturnsValue operon1, ReturnsValue operon2,
			OperatorTypes operator, LineInfo line) {
		this.operator_type = operator;
		this.operon1 = operon1;
		this.operon2 = operon2;
		this.line = line;
	}

	@Override
	public LineInfo getLineNumber() {
		return line;
	}

	@Override
	public Object getValueImpl(VariableContext f, RuntimeExecutable<?> main)
			throws RuntimePascalException {
		Object value1 = operon1.getValue(f, main);
		Object value2 = operon2.getValue(f, main);
		return operate(value1, value2);
	}

	public abstract Object operate(Object value1, Object value2)
			throws PascalArithmeticException, InternalInterpreterException;

	@Override
	public String toString() {
		return "(" + operon1 + ") " + operator_type + " (" + operon2 + ')';
	}

	@Override
	public Object compileTimeValue(CompileTimeContext context)
			throws ParsingException {
		Object value1 = operon1.compileTimeValue(context);
		Object value2 = operon2.compileTimeValue(context);
		if (value1 != null && value2 != null) {
			try {
				return operate(value1, value2);
			} catch (PascalArithmeticException e) {
				throw new ConstantCalculationException(e);
			} catch (InternalInterpreterException e) {
				throw new ConstantCalculationException(e);
			}
		} else {
			return null;
		}
	}

	@Override
	public SetValueExecutable createSetValueInstruction(ReturnsValue r)
			throws UnassignableTypeException {
		throw new UnassignableTypeException(r);
	}

	/* Boy, templates or macros like C++ sure would be useful now... */
	public static BinaryOperatorEvaluation generateOp(ExpressionContext f,
			ReturnsValue v1, ReturnsValue v2, OperatorTypes op_type,
			LineInfo line) throws ParsingException {
		DeclaredType t1 = v1.get_type(f).declType;
		DeclaredType t2 = v2.get_type(f).declType;
		if (!(t1 instanceof JavaClassBasedType && t2 instanceof JavaClassBasedType)) {
			throw new BadOperationTypeException(line, t1, t2, v1, v2, op_type);
		}
		if (t1 == JavaClassBasedType.StringBuilder
				|| t2 == JavaClassBasedType.StringBuilder) {
			if (op_type != OperatorTypes.PLUS
					&& (t1 != JavaClassBasedType.StringBuilder || t2 != JavaClassBasedType.StringBuilder)) {
				throw new BadOperationTypeException(line, t1, t2, v1, v2,
						op_type);
			}
			v1 = new TypeConverter.AnyToString(v1);
			v2 = new TypeConverter.AnyToString(v2);
			return new StringBiOperatorEval(v1, v2, op_type, line);
		}
		if (t1 == JavaClassBasedType.Double || t2 == JavaClassBasedType.Double) {
			v1 = TypeConverter.forceConvertRequired(JavaClassBasedType.Double,
					v1, (JavaClassBasedType) t1);
			v2 = TypeConverter.forceConvertRequired(JavaClassBasedType.Double,
					v2, (JavaClassBasedType) t2);
			return new DoubleBiOperatorEval(v1, v2, op_type, line);
		}
		if (t1 == JavaClassBasedType.Long || t2 == JavaClassBasedType.Long) {
			v1 = TypeConverter.forceConvertRequired(JavaClassBasedType.Long,
					v1, (JavaClassBasedType) t1);
			v2 = TypeConverter.forceConvertRequired(JavaClassBasedType.Long,
					v2, (JavaClassBasedType) t2);
			return new LongBiOperatorEval(v1, v2, op_type, line);
		}
		if (t1 == JavaClassBasedType.Integer
				|| t2 == JavaClassBasedType.Integer) {
			v1 = TypeConverter.forceConvertRequired(JavaClassBasedType.Integer,
					v1, (JavaClassBasedType) t1);
			v2 = TypeConverter.forceConvertRequired(JavaClassBasedType.Integer,
					v2, (JavaClassBasedType) t2);
			return new IntBiOperatorEval(v1, v2, op_type, line);
		}
		if (t1 == JavaClassBasedType.Character
				|| t2 == JavaClassBasedType.Character) {
			v1 = TypeConverter.forceConvertRequired(
					JavaClassBasedType.Character, v1, (JavaClassBasedType) t1);
			v2 = TypeConverter.forceConvertRequired(
					JavaClassBasedType.Character, v2, (JavaClassBasedType) t2);
			return new CharBiOperatorEval(v1, v2, op_type, line);
		}
		if (t1 == JavaClassBasedType.Boolean
				|| t2 == JavaClassBasedType.Boolean) {
			v1 = TypeConverter.forceConvertRequired(JavaClassBasedType.Boolean,
					v1, (JavaClassBasedType) t1);
			v2 = TypeConverter.forceConvertRequired(JavaClassBasedType.Boolean,
					v2, (JavaClassBasedType) t2);
			return new BoolBiOperatorEval(v1, v2, op_type, line);
		}
		throw new BadOperationTypeException(line, t1, t2, v1, v2, op_type);
	}
}
