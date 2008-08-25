package preprocessed.interpreting_objects;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;

import preprocessed.function_declaration;
import preprocessed.variable_declaration;
import preprocessed.instructions.executable;
import preprocessed.interpreting_objects.variables.contains_variables;
import processing.pascal_program;

public class function_on_stack implements contains_variables {
	public HashMap<String, Object> variables = new HashMap<String, Object>();

	public function_declaration prototype;

	public pascal_program program;

	public function_on_stack(pascal_program program,
		function_declaration declaration, Object[] arguments) {
		this.prototype = declaration;
		this.program = program;
		if (prototype.local_variables != null) {
			for (variable_declaration v : prototype.local_variables) {
				v.initialize(variables);
			}
		}
		if (declaration.return_type != null) {
			try {
				variables.put("result", declaration.return_type
						.newInstance());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		this.program = program;
		this.prototype = declaration;
	}

	public Object execute() {
		ListIterator<variable_declaration> i = prototype.local_variables.listIterator();
		for (variable_declaration p = null; i.hasNext(); p = i.next()) {
			try {
				this.variables.put(prototype.variable_names[i.previousIndex()], p
						.type.newInstance());
			} catch (InstantiationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		for (executable e : prototype.instructions) {
			e.execute(this);
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
