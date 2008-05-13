package pascalTypes;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.TreeMap;
import java.util.Map.Entry;

import preprocessed.variable_declaration;

public class custom_type implements pascal_type {
	public custom_type(LinkedList<variable_declaration> types) {
		
		for (Entry<String, Class> e : types.entrySet()) {
			value.put(e.getKey(), new standard_var(e.getValue()));
		}
	}
}
