package preprocessed;

import java.util.HashMap;
import java.util.Map;

import serp.bytecode.BCClass;

public class variable_declaration {
	public String name;

	public Class type;

	public String get_name() {
		return name;
	}

	public variable_declaration(String name, Class type) {
		this.name = name;
		this.type = type;
	}

	public void add_declaration(BCClass c) {
		c.declareField(name, type);
	}

	public void initialize(Map<String, Object> map) {
		try {
			Object value=default_values.get(type);
			if(value==null) {
				value=type.newInstance();
			}
			map.put(name, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	static HashMap<Class, Object> default_values=new HashMap<Class, Object>();
	static {
		default_values.put(Integer.class, 0);
		default_values.put(String.class, "");
		default_values.put(Double.class, 0.0D);
		//TODO add more
	}
}
