package preprocessed;

import java.util.HashMap;

import pascalTypes.pascal_type;

public interface variable_declaration {
	public String get_name();

	public void initialize(HashMap<String, Object> variables);
}
