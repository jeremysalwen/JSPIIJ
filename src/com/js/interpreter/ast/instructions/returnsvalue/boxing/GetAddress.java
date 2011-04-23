package com.js.interpreter.ast.instructions.returnsvalue.boxing;

import com.js.interpreter.ast.CompileTimeContext;
import com.js.interpreter.ast.ExpressionContext;
import com.js.interpreter.ast.instructions.SetValueExecutable;
import com.js.interpreter.ast.instructions.returnsvalue.ConstantAccess;
import com.js.interpreter.ast.instructions.returnsvalue.DebuggableReturnsValue;
import com.js.interpreter.ast.instructions.returnsvalue.ReturnsValue;
import com.js.interpreter.exceptions.ParsingException;
import com.js.interpreter.exceptions.UnassignableTypeException;
import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.pascaltypes.PointerType;
import com.js.interpreter.pascaltypes.RuntimeType;
import com.js.interpreter.runtime.Reference;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public class GetAddress extends DebuggableReturnsValue {
	final ReturnsValue target;

	final SetValueExecutable setTarget;
	LineInfo line;

	public GetAddress(ReturnsValue target) throws UnassignableTypeException {
		this.target = target;
		setTarget = target.createSetValueInstruction(target);
	}

	@Override
	public RuntimeType get_type(ExpressionContext f) throws ParsingException {
		return new RuntimeType(new PointerType(target.get_type(f).declType),
				false);
	}

	@Override
	public LineInfo getLineNumber() {
		return target.getLineNumber();
	}

	@Override
	public Object compileTimeValue(CompileTimeContext context)
			throws ParsingException {
		return null;
	}

	@Override
	public SetValueExecutable createSetValueInstruction(ReturnsValue r)
			throws UnassignableTypeException {
		throw new UnassignableTypeException(this);
	}

	@Override
	public Object getValueImpl(VariableContext f, RuntimeExecutable<?> main)
			throws RuntimePascalException {
		final ConstantAccess ca = new ConstantAccess(null, line);
		final VariableContext ff = f;
		final RuntimeExecutable<?> fmain = main;
		setTarget.setAssignedValue(ca);
		return new Reference() {

			@Override
			public void set(Object value) {
				ca.constant_value = value;
				try {
					setTarget.execute(ff, fmain);
				} catch (RuntimePascalException e) {
					e.printStackTrace();
				}
			}

			@Override
			public Object get() throws RuntimePascalException {
				return target.getValue(ff, fmain);
			}

			@Override
			public Reference clone() {
				return this;
			}
		};
	}
}
