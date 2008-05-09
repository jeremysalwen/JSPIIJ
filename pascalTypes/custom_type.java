package pascalTypes;

import java.util.LinkedList;
import java.util.TreeMap;
import java.util.Map.Entry;

public class custom_type extends pascalType<TreeMap<String, pascalVar>> {
	public custom_type(TreeMap<String, Class> types) {
		for (Entry<String, Class> e : types.entrySet()) {
			value.put(e.getKey(), new pascalVar(e.getValue()));
		}
	}
}
