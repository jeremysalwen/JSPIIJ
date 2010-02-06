package edu.js.interpreter.preprocessed.instructions.returnsvalue;

import edu.js.interpreter.pascaltypes.JavaClassBasedType;
import edu.js.interpreter.pascaltypes.PascalType;
import edu.js.interpreter.preprocessed.FunctionDeclaration;
import edu.js.interpreter.preprocessed.interpretingobjects.FunctionOnStack;

public class BuiltinTypeConversion implements ReturnsValue {
	PascalType output;
	ReturnsValue input;
	public BuiltinTypeConversion(PascalType output, ReturnsValue input) {
		this.output=output;
		this.input=input;
	}
	@Override
	public PascalType get_type(FunctionDeclaration f) {
		return output;
	}

	@Override
	public Object get_value(FunctionOnStack f) {
		Object value=input.get_value(f);
		if(output==JavaClassBasedType.Integer) {
			return ((Number)value).intValue();
		}
		if(output==JavaClassBasedType.Double) {
			return ((Number)value).doubleValue();
		}
		//TODO add more conversions
		System.err.println("Not able to automatically convert type "+output);
		return value;
	}

}
