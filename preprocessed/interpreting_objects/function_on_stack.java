package preprocessed.interpreting_objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import preprocessed.function_declaration;
import preprocessed.variable_declaration;
import preprocessed.instructions.executable;
import preprocessed.interpreting_objects.variables.contains_variables;
import processing.pascal_program;

public class function_on_stack implements contains_variables {
	public HashMap<String, Object> variables = new HashMap<String, Object>();

	public function_declaration prototype;

	public pascal_program program;

	List<pointer> var_ptrs;
	public function_on_stack(pascal_program program,
			function_declaration declaration, Object[] arguments) {
		this.prototype = declaration;
		this.program = program;
		if (prototype.local_variables != null) {
			for (variable_declaration v : prototype.local_variables) {
				v.initialize(variables);
			}
		}
		var_ptrs=new ArrayList<pointer>();
		for (int i = 0; i < arguments.length; i++) {
			if(prototype.are_varargs.get(i)) {
				var_ptrs.add((pointer) arguments[i]);
				arguments[i]=((pointer)arguments[i]).value;				
			}
			variables.put(prototype.argument_names.get(i), arguments[i]);
		}
		if (declaration.return_type != null) {
				new variable_declaration("result",prototype.return_type).initialize(variables);
		}
		this.program = program;
		this.prototype = declaration;
	}

	public Object execute() {
		for (executable e : prototype.instructions) {
			e.execute(this);
		}
		int j=0;
		for (int i = 0; i < prototype.are_varargs.size(); i++) {
			if(prototype.are_varargs.get(i)) {
				var_ptrs.get(j++).value=get_var(prototype.argument_names.get(i));
			}
		}
		return get_var("result");
	}

	public Object get_var(String name) {
		return variables.get(name);
	}

	public void set_var(String name, Object val) {
		variables.put(name, val);
	}

}
