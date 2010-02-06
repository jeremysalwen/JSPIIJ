package edu.js.interpreter.preprocessed;

import java.util.Map;

import serp.bytecode.Code;
import edu.js.interpreter.pascaltypes.PascalType;

public class VariableDeclaration {
	public String name;

	public PascalType type;

	public String get_name() {
		return name;
	}

	public VariableDeclaration(String name, PascalType type) {
		this.name = name;
		this.type = type;
	}


	public void initialize(Map<String, Object> map) {
		map.put(name, type.initialize());
	}

	public void add_to_constructor(Code constructor_code) {
		constructor_code.aload().setThis();
		type.get_default_value_on_stack(constructor_code);
		constructor_code.putfield().setField(name,
				constructor_code.getMethod().getDeclarer());
	}

	@Override
	public int hashCode() {
		return name.hashCode()* 31+type.hashCode();
	}
}
