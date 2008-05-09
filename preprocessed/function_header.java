package preprocessed;

import java.util.LinkedList;
import java.util.Map.Entry;

public class function_header {
	public String name;
	public Class return_type;
	public LinkedList<variable_declaration> arguments;

	public function_header(String name,
			LinkedList<variable_declaration> arguments, Class return_type) {
		this.name=name;
		this.arguments=arguments;
		this.return_type=return_type;
	}
	
	@Override
	public int hashCode() {
		int result=0;
		for(variable_declaration v:arguments) {
			result^=v.hashCode();
			result >>=v.hashCode();
		}
	}
}
