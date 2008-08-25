package preprocessed.instructions;

import java.util.ListIterator;

import preprocessed.instructions.returns_value.returns_value;
import preprocessed.interpreting_objects.function_on_stack;
import preprocessed.interpreting_objects.variables.contains_variables;
import preprocessed.interpreting_objects.variables.variable_identifier;

public class variable_set implements executable {
	variable_identifier name;

	returns_value value;

	public variable_set(variable_identifier name, returns_value value) {
		this.name = name;
		this.value = value;
	}

	public void execute(function_on_stack f) {
		ListIterator<String> iterator = name.listIterator();
		contains_variables v = f;
		for (int i = 0; i < name.size() - 1; v = (contains_variables) v
				.get_var(iterator.next()))
			;
		v.set_var(iterator.next(), value.get_value(f));
	}

	@Override
	public String toString() {
		return "set [" + name + "] to [" + value + "]\n";
	}
}
