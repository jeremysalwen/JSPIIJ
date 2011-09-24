package com.js.interpreter.plugins.standard;

import java.lang.reflect.Array;

import com.js.interpreter.ast.CompileTimeContext;
import com.js.interpreter.ast.ExpressionContext;
import com.js.interpreter.ast.instructions.Executable;
import com.js.interpreter.ast.instructions.SetValueExecutable;
import com.js.interpreter.ast.returnsvalue.FunctionCall;
import com.js.interpreter.ast.returnsvalue.ReturnsValue;
import com.js.interpreter.exceptions.ParsingException;
import com.js.interpreter.exceptions.UnassignableTypeException;
import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.pascaltypes.ArgumentType;
import com.js.interpreter.pascaltypes.ArrayType;
import com.js.interpreter.pascaltypes.BasicType;
import com.js.interpreter.pascaltypes.DeclaredType;
import com.js.interpreter.pascaltypes.PointerType;
import com.js.interpreter.pascaltypes.RuntimeType;
import com.js.interpreter.pascaltypes.SubrangeType;
import com.js.interpreter.plugins.templated.TemplatedPascalPlugin;
import com.js.interpreter.runtime.VariableBoxer;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public class SetLength implements TemplatedPascalPlugin {

	static ArgumentType[] _argumentTypes = {
			new RuntimeType(new ArrayType<DeclaredType>(
					BasicType.anew(Object.class), new SubrangeType(0,
							0)), true),
			new RuntimeType(BasicType.Integer, false) };

	@Override
	public String name() {
		return "setlength";
	}

	@Override
	public FunctionCall generateCall(LineInfo line, ReturnsValue[] arguments,
			ExpressionContext f) throws ParsingException {
		ReturnsValue array = arguments[0];
		ReturnsValue size = arguments[1];
		@SuppressWarnings("rawtypes")
		DeclaredType elemtype = ((ArrayType) ((PointerType)array.get_type(f).declType).pointedToType).element_type;
		LineInfo l = line;
		return new SetLengthCall(array, size, elemtype, l);
	}

	@Override
	public FunctionCall generatePerfectFitCall(LineInfo line,
			ReturnsValue[] values, ExpressionContext f) throws ParsingException {
		return generateCall(line, values, f);
	}

	@Override
	public ArgumentType[] argumentTypes() {
		return _argumentTypes;
	}

	@Override
	public DeclaredType return_type() {
		return null;
	}

	class SetLengthCall extends FunctionCall {
		ReturnsValue array;
		ReturnsValue size;
		DeclaredType elemtype;

		LineInfo line;

		public SetLengthCall(ReturnsValue array, ReturnsValue size,
				DeclaredType elemType, LineInfo line) {
			this.array = array;
			this.size = size;
			this.elemtype = elemType;
			this.line = line;
		}

		@Override
		public RuntimeType get_type(ExpressionContext f)
				throws ParsingException {
			return null;
		}

		@Override
		public LineInfo getLineNumber() {
			return line;
		}

		@Override
		public SetValueExecutable createSetValueInstruction(ReturnsValue r)
				throws UnassignableTypeException {
			throw new UnassignableTypeException(this);
		}

		@Override
		public Object compileTimeValue(CompileTimeContext context) {
			return null;
		}

		@Override
		public ReturnsValue compileTimeExpressionFold(CompileTimeContext context)
				throws ParsingException {
			return new SetLengthCall(array.compileTimeExpressionFold(context),
					size.compileTimeExpressionFold(context), elemtype, line);
		}

		@Override
		public Executable compileTimeConstantTransform(CompileTimeContext c)
				throws ParsingException {
			return new SetLengthCall(array.compileTimeExpressionFold(c),
					size.compileTimeExpressionFold(c), elemtype, line);
		}

		@Override
		protected String getFunctionName() {
			return "setlength";
		}

		@Override
		public Object getValueImpl(VariableContext f, RuntimeExecutable<?> main)
				throws RuntimePascalException {
			int s = (Integer) size.getValue(f, main);
			@SuppressWarnings("rawtypes")
			VariableBoxer a = (VariableBoxer)array.getValue(f, main);
			Object arr=a.get();
			int oldlength = Array.getLength(arr);
			Object newarr = Array.newInstance(elemtype.getTransferClass(), s);
			if (oldlength > s) {
				System.arraycopy(arr, 0, newarr, 0, s);
			} else {
				System.arraycopy(arr, 0, newarr, 0, oldlength);
				for (int i = oldlength; i < s; i++) {
					Array.set(newarr, i, elemtype.initialize());
				}
			}
			a.set(newarr);
			return null;
		}
	}
}
