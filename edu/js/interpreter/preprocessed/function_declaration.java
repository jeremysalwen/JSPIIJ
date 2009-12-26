package edu.js.interpreter.preprocessed;

import java.util.ArrayList;
import java.util.List;

import edu.js.interpreter.pascal_types.pascal_type;
import edu.js.interpreter.preprocessed.instructions.downto_for_statement;
import edu.js.interpreter.preprocessed.instructions.executable;
import edu.js.interpreter.preprocessed.instructions.for_statement;
import edu.js.interpreter.preprocessed.instructions.if_statement;
import edu.js.interpreter.preprocessed.instructions.instruction_grouper;
import edu.js.interpreter.preprocessed.instructions.repeat_instruction;
import edu.js.interpreter.preprocessed.instructions.variable_set;
import edu.js.interpreter.preprocessed.instructions.while_statement;
import edu.js.interpreter.preprocessed.instructions.returns_value.binary_operator_evaluation;
import edu.js.interpreter.preprocessed.instructions.returns_value.constant_access;
import edu.js.interpreter.preprocessed.instructions.returns_value.returns_value;
import edu.js.interpreter.preprocessed.instructions.returns_value.unary_operator_evaluation;
import edu.js.interpreter.preprocessed.instructions.returns_value.variable_access;
import edu.js.interpreter.preprocessed.interpreting_objects.function_on_stack;
import edu.js.interpreter.preprocessed.interpreting_objects.variables.returnsvalue_subvar_identifier;
import edu.js.interpreter.preprocessed.interpreting_objects.variables.string_subvar_identifier;
import edu.js.interpreter.preprocessed.interpreting_objects.variables.variable_identifier;
import edu.js.interpreter.processing.StaticMethods;
import edu.js.interpreter.processing.pascal_program;
import edu.js.interpreter.tokens.EOF_token;
import edu.js.interpreter.tokens.token;
import edu.js.interpreter.tokens.basic.assignment_token;
import edu.js.interpreter.tokens.basic.colon_token;
import edu.js.interpreter.tokens.basic.comma_token;
import edu.js.interpreter.tokens.basic.do_token;
import edu.js.interpreter.tokens.basic.downto_token;
import edu.js.interpreter.tokens.basic.else_token;
import edu.js.interpreter.tokens.basic.for_token;
import edu.js.interpreter.tokens.basic.if_token;
import edu.js.interpreter.tokens.basic.period_token;
import edu.js.interpreter.tokens.basic.repeat_token;
import edu.js.interpreter.tokens.basic.semicolon_token;
import edu.js.interpreter.tokens.basic.then_token;
import edu.js.interpreter.tokens.basic.to_token;
import edu.js.interpreter.tokens.basic.until_token;
import edu.js.interpreter.tokens.basic.var_token;
import edu.js.interpreter.tokens.basic.while_token;
import edu.js.interpreter.tokens.grouping.begin_end_token;
import edu.js.interpreter.tokens.grouping.bracketed_token;
import edu.js.interpreter.tokens.grouping.grouper_token;
import edu.js.interpreter.tokens.grouping.parenthesized_token;
import edu.js.interpreter.tokens.value.double_token;
import edu.js.interpreter.tokens.value.integer_token;
import edu.js.interpreter.tokens.value.operator_token;
import edu.js.interpreter.tokens.value.string_token;
import edu.js.interpreter.tokens.value.word_token;

public class function_declaration extends abstract_function {
	public pascal_program program;

	public String name;

	public List<variable_declaration> local_variables;

	public executable instructions;

	public pascal_type return_type;

	/* These go together ----> */
	public String[] argument_names;

	public pascal_type[] argument_types;

	public boolean[] are_varargs;

	/* <----- */

	public function_declaration(pascal_program p, grouper_token i,
			boolean is_procedure) {
		this.program = p;
		instructions = new instruction_grouper();
		name = program.get_word_value(i);
		get_arguments_for_declaration(i, is_procedure);
		token next = i.peek();
		assert (is_procedure ^ (next instanceof colon_token));
		if (!is_procedure && next instanceof colon_token) {
			try {
				i.take();
				return_type = program.get_next_pascal_type(i);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		program.assert_next_semicolon(i);
		next = i.peek();
		if (next instanceof var_token) {
			i.take();
			local_variables = program.get_variable_declarations(i);
		} else {
			local_variables = new ArrayList<variable_declaration>();
		}
		instructions = get_next_command(i);
		program.assert_next_semicolon(i);
	}

	public void add_local_variable(variable_declaration v) {
		local_variables.add(v);
	}

	public function_declaration(pascal_program p) {
		this.program = p;
		this.local_variables = new ArrayList<variable_declaration>();
	}

	public function_declaration(List<variable_declaration> local_variables,
			instruction_grouper instructions) {
		this.local_variables = local_variables;
		this.instructions = instructions;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public Object call(pascal_program program, Object[] arguments) {
		return new function_on_stack(program, this, arguments).execute();
	}

	public pascal_type get_variable_type(String name) {
		int index = StaticMethods.indexOf(argument_names, name);
		if (index != -1) {
			return argument_types[StaticMethods.indexOf(argument_names, name)];
		} else {
			for (variable_declaration v : local_variables) {
				if (v.name.equals(name)) {
					return v.type;
				}
			}
		}
		return null;
	}

	@Override
	public boolean is_varargs(int i) {
		return are_varargs[i];
	}

	@Override
	public String toString() {
		return super.toString() + ",function";
	}

	private void get_arguments_for_declaration(grouper_token i,
			boolean is_procedure) { // need
		List<Boolean> are_varargs_list = new ArrayList<Boolean>();
		List<String> names_list = new ArrayList<String>();
		List<pascal_type> types_list = new ArrayList<pascal_type>();
		token next = i.take();
		if (next instanceof parenthesized_token) {
			parenthesized_token arguments_token = (parenthesized_token) next;
			while (arguments_token.hasNext()) {
				int j = 0; // counts number added of this type
				next = arguments_token.take();
				boolean is_varargs = false;
				if (next instanceof var_token) {
					is_varargs = true;
					next = arguments_token.take();
				}
				do {
					are_varargs_list.add(is_varargs);
					names_list.add(((word_token) next).name);
					j++;
				} while ((next = arguments_token.take()) instanceof comma_token);

				assert (next instanceof colon_token);
				pascal_type type;
				try {
					type = program.get_next_pascal_type(arguments_token);
					while (j > 0) {
						types_list.add(type);
						j--;
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		argument_types = types_list.toArray(new pascal_type[types_list.size()]);
		argument_names = names_list.toArray(new String[names_list.size()]);
		are_varargs = new boolean[are_varargs_list.size()];
		for (int k = 0; k < are_varargs.length; k++) {
			are_varargs[k] = are_varargs_list.get(k);
		}
	}

	returns_value get_next_returns_value(grouper_token iterator) {
		token next = iterator.take();
		if (next instanceof operator_token) {
			assert ((operator_token) next).can_be_unary();
			return new unary_operator_evaluation(
					get_next_returns_value(iterator),
					((operator_token) next).type);
		}
		returns_value result = null;
		if (next instanceof parenthesized_token) {
			result = get_single_value((parenthesized_token) next);
		} else if (next instanceof integer_token) {
			result = new constant_access(((integer_token) next).value);
		} else if (next instanceof double_token) {
			result = new constant_access(((double_token) next).value);
		} else if (next instanceof string_token) {
			result = new constant_access(new StringBuilder(
					((string_token) next).value));
		} else if (next instanceof word_token) {
			String name = ((word_token) next).name;
			if (!((next = iterator.peek()) instanceof EOF_token)) {
				if (next instanceof parenthesized_token) {
					returns_value[] arguments = get_arguments_for_call((parenthesized_token) iterator
							.take());
					result = program.generate_function_call(name, arguments,
							this);
				} else {
					result = new variable_access(get_next_var_identifier(name,
							iterator));
				}
			} else {
				result = new variable_access(get_next_var_identifier(name,
						iterator));
			}
		}
		assert (result != null);
		if ((next = iterator.peek()) instanceof operator_token) {
			iterator.take();
			result = new binary_operator_evaluation(result,
					get_next_returns_value(iterator),
					((operator_token) next).type);
		}
		return result;
	}

	variable_identifier get_next_var_identifier(grouper_token i) {
		token next;
		variable_identifier identifier = new variable_identifier();
		identifier.add(new string_subvar_identifier(program.get_word_value(i)));
		while (true) {
			if (i.peek() instanceof period_token) {
				i.take();
				next = i.take();
				if (next instanceof word_token) {
					identifier.add(new string_subvar_identifier(
							((word_token) next).name));
				} else {
					program.error();
				}
			} else if (i.peek() instanceof bracketed_token) {
				identifier.add(new returnsvalue_subvar_identifier(
						get_next_returns_value((bracketed_token) i.take())));
			} else {
				break;
			}
		}
		return identifier;
	}

	variable_identifier get_next_var_identifier(String initial, grouper_token i) {
		token next;
		variable_identifier identifier = new variable_identifier();
		identifier.add(new string_subvar_identifier(initial));
		while (true) {
			if (i.peek() instanceof period_token) {
				i.take();
				next = i.take();
				if (next instanceof word_token) {
					identifier.add(new string_subvar_identifier(
							((word_token) next).name));
				} else {
					program.error();
				}
			} else if (i.peek() instanceof bracketed_token) {
				identifier.add(new returnsvalue_subvar_identifier(
						get_next_returns_value((bracketed_token) i.take())));
			} else {
				break;
			}
		}
		return identifier;
	}

	pascal_type[] returns_values_to_pascal_types(returns_value[] values) {
		pascal_type[] result = new pascal_type[values.length];
		for (int i = 0; i < values.length; i++) {
			result[i] = values[i].get_type(this);
		}
		return result;
	}

	public executable get_next_command(grouper_token token_iterator) {
		token next = token_iterator.take();
		if (next instanceof if_token) {
			returns_value condition = get_next_returns_value(token_iterator);
			next = token_iterator.take();
			assert (next instanceof then_token);
			executable command = get_next_command(token_iterator);
			executable else_command = null;
			next = token_iterator.peek();
			if (next instanceof else_token) {
				token_iterator.take();
				else_command = get_next_command(token_iterator);
			}
			assert (next instanceof semicolon_token);
			return new if_statement(condition, command, else_command);
		} else if (next instanceof while_token) {
			returns_value condition = get_next_returns_value(token_iterator);
			next = token_iterator.take();
			assert (next instanceof do_token);
			executable command = get_next_command(token_iterator);
			program.assert_next_semicolon(token_iterator);
			return new while_statement(condition, command);
		} else if (next instanceof begin_end_token) {
			instruction_grouper begin_end_preprocessed = new instruction_grouper();
			begin_end_token cast_token = (begin_end_token) next;
			while (cast_token.hasNext()) {
				begin_end_preprocessed
						.add_command(get_next_command(cast_token));
				program.assert_next_semicolon(cast_token);
			}
			return begin_end_preprocessed;
		} else if (next instanceof for_token) {
			variable_identifier tmp_var = get_next_var_identifier(token_iterator);
			next = token_iterator.take();
			assert (next instanceof assignment_token);
			returns_value first_value = get_next_returns_value(token_iterator);
			next = token_iterator.take();
			boolean downto = false;
			if (next instanceof downto_token) {
				downto = true;
			} else if (!(next instanceof to_token)) {
				System.err.println("Expected to or downto");
				System.exit(1);
			}
			returns_value last_value = get_next_returns_value(token_iterator);
			next = token_iterator.take();
			assert (next instanceof do_token);
			executable result;
			if (downto) { // TODO probably should merge these two types
				result = new downto_for_statement(tmp_var, first_value,
						last_value, get_next_command(token_iterator));
			} else {
				result = new for_statement(tmp_var, first_value, last_value,
						get_next_command(token_iterator));
			}
			return result;
		} else if (next instanceof repeat_token) {
			instruction_grouper command = new instruction_grouper();
			while (!(token_iterator.peek_no_EOF() instanceof until_token)) {
				get_next_command(token_iterator);
			}
			next = token_iterator.take();
			assert (next instanceof until_token);
			returns_value condition = get_next_returns_value(token_iterator);
			return new repeat_instruction(command, condition);
		} else if (next instanceof word_token) {
			String name = program.get_word_value(next);
			next = token_iterator.peek_no_EOF();
			if (next instanceof parenthesized_token) {
				token_iterator.take();
				returns_value[] arguments = get_arguments_for_call((parenthesized_token) next);
				return program.generate_function_call(
						name, arguments, this);
			} else {
				// at this point assuming it is a variable identifier.
				variable_identifier identifier = get_next_var_identifier(name,
						token_iterator);
				next = token_iterator.take();
				assert (next instanceof assignment_token);
				returns_value value_to_assign = get_next_returns_value(token_iterator);
				return new variable_set(identifier, value_to_assign);
			}
		}
		System.err
				.println("Could not identify token "
						+ next
						+ ", because it did not match any of the criteria for an instruction");
		return null;
	}

	returns_value[] get_arguments_for_call(parenthesized_token t) {
		List<returns_value> result = new ArrayList<returns_value>();
		while (t.hasNext()) {
			result.add(get_next_returns_value(t));
			if (t.hasNext()) {
				token next = t.take();
				assert (next instanceof comma_token);
			}
		}
		return result.toArray(new returns_value[result.size()]);
	}

	returns_value get_single_value(parenthesized_token t) {
		returns_value result = get_next_returns_value(t);
		assert (!t.hasNext());
		return result;
	}

	private List<executable> get_function_body(begin_end_token t) {
		List<executable> commands = new ArrayList<executable>();
		while (t.hasNext()) {
			commands.add(get_next_command(t));
			program.assert_next_semicolon(t);
		}
		return commands;
	}

	@Override
	public pascal_type[] argument_types() {
		return argument_types;
	}

	@Override
	public pascal_type return_type() {
		return return_type;
	}
}
