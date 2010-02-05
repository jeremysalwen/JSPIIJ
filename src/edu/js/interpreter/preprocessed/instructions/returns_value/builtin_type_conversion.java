package edu.js.interpreter.preprocessed.instructions.returns_value;

import edu.js.interpreter.pascal_types.class_pascal_type;
import edu.js.interpreter.pascal_types.pascal_type;
import edu.js.interpreter.preprocessed.function_declaration;
import edu.js.interpreter.preprocessed.interpreting_objects.function_on_stack;

public class builtin_type_conversion implements returns_value {
	pascal_type output;
	returns_value input;
	public builtin_type_conversion(pascal_type output, returns_value input) {
		this.output=output;
		this.input=input;
	}
	@Override
	public pascal_type get_type(function_declaration f) {
		return output;
	}

	@Override
	public Object get_value(function_on_stack f) {
		Object value=input.get_value(f);
		if(output==class_pascal_type.Integer) {
			return ((Number)value).intValue();
		}
		if(output==class_pascal_type.Double) {
			return ((Number)value).doubleValue();
		}
		//TODO add more conversions
		System.err.println("Not able to automatically convert type "+output);
		return value;
	}

}
