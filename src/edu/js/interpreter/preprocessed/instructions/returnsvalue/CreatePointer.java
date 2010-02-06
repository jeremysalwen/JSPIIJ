package edu.js.interpreter.preprocessed.instructions.returnsvalue;

import java.util.List;

import edu.js.interpreter.pascaltypes.CustomType;
import edu.js.interpreter.pascaltypes.PascalType;
import edu.js.interpreter.preprocessed.FunctionDeclaration;
import edu.js.interpreter.preprocessed.VariableDeclaration;
import edu.js.interpreter.preprocessed.interpretingobjects.ArrayPointer;
import edu.js.interpreter.preprocessed.interpretingobjects.ContainsVariablesPointer;
import edu.js.interpreter.preprocessed.interpretingobjects.FunctionOnStack;
import edu.js.interpreter.preprocessed.interpretingobjects.variables.ContainsVariables;
import edu.js.interpreter.preprocessed.interpretingobjects.variables.SubvarIdentifier;

public class CreatePointer implements ReturnsValue {
	VariableAccess container;

	SubvarIdentifier index;

	public CreatePointer(VariableAccess container, SubvarIdentifier index) {
		this.container = container;
		this.index = index;
	}

	public PascalType get_type(FunctionDeclaration f) {
		PascalType container_type = container.get_type(f);
		if (container_type.isarray()) {
			return container_type.get_type_array().element_type;
		} else {
			CustomType custom_type = ((CustomType) container_type);
			List<VariableDeclaration> subtypes = custom_type.variable_types;
			for (VariableDeclaration v : subtypes) {
				if (v.name.equals(index.string())) {
					return v.type;
				}
			}
			System.err.println("Error, could not find subvar type "
					+ index.string());
			return null;
		}
	}

	public Object get_value(FunctionOnStack f) {
		Object value = container.get_value(f);
		if (index.isreturnsvalue()) {
			Integer ind = (Integer) index.returnsvalue().get_value(f);
			assert (value.getClass().isArray());
			return new ArrayPointer(value, ind);
		} else {
			return new ContainsVariablesPointer((ContainsVariables) value,
					index.string());
		}
	}
}
