package edu.js.interpreter.preprocessed.instructions.returnsvalue;

import edu.js.interpreter.pascaltypes.PascalType;
import edu.js.interpreter.preprocessed.AbstractFunction;
import edu.js.interpreter.preprocessed.FunctionDeclaration;
import edu.js.interpreter.preprocessed.instructions.Executable;
import edu.js.interpreter.preprocessed.interpretingobjects.ArrayPointer;
import edu.js.interpreter.preprocessed.interpretingobjects.ContainsVariablesPointer;
import edu.js.interpreter.preprocessed.interpretingobjects.FunctionOnStack;
import edu.js.interpreter.preprocessed.interpretingobjects.variables.ContainsVariables;
import edu.js.interpreter.preprocessed.interpretingobjects.variables.SubvarIdentifier;
import edu.js.interpreter.preprocessed.interpretingobjects.variables.VariableIdentifier;

public class AbstractFunctionCall implements ReturnsValue, Executable {
	AbstractFunction function;

	ReturnsValue[] arguments;

	public AbstractFunctionCall(AbstractFunction function,
			ReturnsValue[] arguments) {
		this.function = function;
		if(function==null) {
			System.err.println("Warning: Null function call");
		}
		this.arguments = arguments;
	}

	public Object get_value(FunctionOnStack f) {
		PascalType[] arg_types = new PascalType[arguments.length];
		Object[] values = new Object[arguments.length];
		for (int i = 0; i < arguments.length; i++) {
			arg_types[i] = arguments[i].get_type(f.prototype);
			if (arg_types[i] == null || arg_types[i].toclass() == null) {
				System.out.println(arguments[i]);
				arguments[i].get_type(f.prototype);
			}
		}
		for (int i = 0; i < values.length; i++) {
			if (function.is_varargs(i)) {
				if (!(arguments[i] instanceof VariableAccess)) {
					System.err
							.println("Attempted to pass non-variable as variable argument");
					System.exit(0);
				}
				VariableIdentifier a = (VariableIdentifier) arguments[i];
				Object var_holder = f.get_variable_holder(a);
				SubvarIdentifier identifier = a.get(a.size() - 1);
				if (identifier.isstring()) {
					values[i] = new ContainsVariablesPointer(
							(ContainsVariables) var_holder, identifier
									.string());
				} else {
					values[i] = new ArrayPointer(var_holder,
							((Number) identifier.returnsvalue().get_value(f))
									.intValue());
				}
			} else {
				values[i] = arguments[i].get_value(f);
				if (values[i] instanceof ContainsVariables) {
					values[i] = ((ContainsVariables) values[i]).clone();
				}
			}
		}
		Object result = function.call(f.program, values);
		return result;
	}

	@Override
	public String toString() {
		return "call function [" + function + "] with args [" + arguments + ']';
	}

	public boolean execute(FunctionOnStack f) {
		get_value(f);
		return false;
	}

	public PascalType get_type(FunctionDeclaration f) {
		PascalType[] arg_types = new PascalType[arguments.length];
		for (int i = 0; i < arguments.length; i++) {
			arg_types[i] = arguments[i].get_type(f);
		}
		return function.return_type();
	}
}
