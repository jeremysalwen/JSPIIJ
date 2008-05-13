package preprocessed;

import java.util.LinkedList;
import java.util.Map.Entry;

public class function_header {
	public String name;
	public Class return_type;
	public LinkedList<variable_declaration> arguments;

	public function_header(String name,
			LinkedList<variable_declaration> arguments, Class return_type) {
		this.name = name;
		this.arguments = arguments;
		this.return_type = return_type;
	}

	@Override
	public int hashCode() {
		int result = arguments.hashCode();
		result *= 31;
		result ^= name.hashCode();
		result *= 31;
		result ^= return_type.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof function_header) {
			function_header f = (function_header) obj;
			return (f.arguments.equals(arguments) && f.name.equals(name) && f.return_type == return_type);
		}
		return false;
	}
}
