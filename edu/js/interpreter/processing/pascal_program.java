package edu.js.interpreter.processing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.js.interpreter.pascal_types.array_type;
import edu.js.interpreter.pascal_types.class_pascal_type;
import edu.js.interpreter.pascal_types.custom_type_declaration;
import edu.js.interpreter.pascal_types.pascal_type;
import edu.js.interpreter.preprocessed.Grouper;
import edu.js.interpreter.preprocessed.abstract_function;
import edu.js.interpreter.preprocessed.custom_type_generator;
import edu.js.interpreter.preprocessed.function_declaration;
import edu.js.interpreter.preprocessed.plugin_declaration;
import edu.js.interpreter.preprocessed.variable_declaration;
import edu.js.interpreter.preprocessed.instructions.downto_for_statement;
import edu.js.interpreter.preprocessed.instructions.executable;
import edu.js.interpreter.preprocessed.instructions.for_statement;
import edu.js.interpreter.preprocessed.instructions.if_statement;
import edu.js.interpreter.preprocessed.instructions.instruction_grouper;
import edu.js.interpreter.preprocessed.instructions.repeat_instruction;
import edu.js.interpreter.preprocessed.instructions.variable_set;
import edu.js.interpreter.preprocessed.instructions.while_statement;
import edu.js.interpreter.preprocessed.instructions.returns_value.abstract_function_call;
import edu.js.interpreter.preprocessed.instructions.returns_value.binary_operator_evaluation;
import edu.js.interpreter.preprocessed.instructions.returns_value.constant_access;
import edu.js.interpreter.preprocessed.instructions.returns_value.returns_value;
import edu.js.interpreter.preprocessed.instructions.returns_value.unary_operator_evaluation;
import edu.js.interpreter.preprocessed.instructions.returns_value.variable_access;
import edu.js.interpreter.preprocessed.interpreting_objects.function_on_stack;
import edu.js.interpreter.preprocessed.interpreting_objects.variables.returnsvalue_subvar_identifier;
import edu.js.interpreter.preprocessed.interpreting_objects.variables.string_subvar_identifier;
import edu.js.interpreter.preprocessed.interpreting_objects.variables.subvar_identifier;
import edu.js.interpreter.preprocessed.interpreting_objects.variables.variable_identifier;
import edu.js.interpreter.tokens.EOF_token;
import edu.js.interpreter.tokens.token;
import edu.js.interpreter.tokens.basic.assignment_token;
import edu.js.interpreter.tokens.basic.colon_token;
import edu.js.interpreter.tokens.basic.comma_token;
import edu.js.interpreter.tokens.basic.do_token;
import edu.js.interpreter.tokens.basic.downto_token;
import edu.js.interpreter.tokens.basic.else_token;
import edu.js.interpreter.tokens.basic.for_token;
import edu.js.interpreter.tokens.basic.function_token;
import edu.js.interpreter.tokens.basic.if_token;
import edu.js.interpreter.tokens.basic.of_token;
import edu.js.interpreter.tokens.basic.period_token;
import edu.js.interpreter.tokens.basic.procedure_token;
import edu.js.interpreter.tokens.basic.repeat_token;
import edu.js.interpreter.tokens.basic.semicolon_token;
import edu.js.interpreter.tokens.basic.then_token;
import edu.js.interpreter.tokens.basic.to_token;
import edu.js.interpreter.tokens.basic.var_token;
import edu.js.interpreter.tokens.basic.while_token;
import edu.js.interpreter.tokens.grouping.base_grouper_token;
import edu.js.interpreter.tokens.grouping.begin_end_token;
import edu.js.interpreter.tokens.grouping.bracketed_token;
import edu.js.interpreter.tokens.grouping.grouper_token;
import edu.js.interpreter.tokens.grouping.parenthesized_token;
import edu.js.interpreter.tokens.grouping.record_token;
import edu.js.interpreter.tokens.grouping.type_token;
import edu.js.interpreter.tokens.value.double_token;
import edu.js.interpreter.tokens.value.integer_token;
import edu.js.interpreter.tokens.value.operator_token;
import edu.js.interpreter.tokens.value.operator_types;
import edu.js.interpreter.tokens.value.string_token;
import edu.js.interpreter.tokens.value.word_token;


public class pascal_program implements Runnable {
	public run_mode mode;

	public function_declaration main;

	public function_on_stack main_running;

	public HashMap<String, custom_type_declaration> custom_types;

	/*
	 * plugins and functions
	 */
	public HashMap<abstract_function, abstract_function> callable_functions;

	public void run() {
		mode = run_mode.running;
		main_running = new function_on_stack(this, main, new Object[0]);
		main_running.execute();
	}

	pascal_program(List<plugin_declaration> plugins) {
		callable_functions = new HashMap<abstract_function, abstract_function>();
		custom_types = new HashMap<String, custom_type_declaration>();
		for (plugin_declaration p : plugins) {
			callable_functions.put(p, p);
		}
		main = new function_declaration();
		main.name = "main";
		main.argument_types = new ArrayList<pascal_type>();
	}

	public pascal_program(String program, List<plugin_declaration> plugins) {
		this(plugins);
		Grouper grouper = new Grouper(program);
		new Thread(grouper).start();
		parse_tree(grouper.token_queue);
		generate_classes();
	}

	void generate_classes() {

	}

	public void parse_tree(base_grouper_token tokens) {
		while (tokens.hasNext()) {
			add_next_declaration(tokens);
		}
	}

	public pascal_program(base_grouper_token tokens,
			List<plugin_declaration> plugins) {
		this(plugins);
		parse_tree(tokens);
	}

	private void add_next_declaration(base_grouper_token i) {
		token next = i.take();
		if (next instanceof procedure_token || next instanceof function_token) {
			boolean is_procedure = next instanceof procedure_token;
			function_declaration declaration = new function_declaration();
			declaration.name = get_word_value(i);
			get_arguments_for_declaration(i, is_procedure,
					declaration.argument_names, declaration.argument_types,
					declaration.are_varargs);
			next = i.take();
			assert (is_procedure ^ next instanceof colon_token);
			if (!is_procedure && next instanceof colon_token) {
				try {
					declaration.return_type = get_next_java_class(i);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
			assert_next_semicolon(i);
			List<variable_declaration> local_variables = null;
			next = i.peek();
			if (next instanceof var_token) {
				i.take();
				local_variables = get_variable_declarations(i);
			}
			List<executable> commands = get_function_body((begin_end_token) i
					.take());
			assert_next_semicolon(i);
			declaration.instructions = commands;

			declaration.local_variables = local_variables == null ? new ArrayList<variable_declaration>()
					: local_variables;
			callable_functions.put(declaration, declaration);
			return;
		} else if (next instanceof type_token) {
			add_custom_type_declaration(i);
			return;
		} else if (next instanceof begin_end_token) {
			main.instructions = get_function_body((begin_end_token) next);
			next = i.take();
			assert (next instanceof period_token);
			return;
		} else if (next instanceof var_token) {
			List<variable_declaration> global_var_decs = get_variable_declarations(i);
			for (variable_declaration v : global_var_decs) {
				main.local_variables.add(v);
			}
			return;
		}
	}

	private void add_custom_type_declaration(grouper_token i) {
		custom_type_declaration result = new custom_type_declaration();
		result.name = get_word_value(i);
		token next = i.take();
		assert (next instanceof operator_token);
		assert ((operator_token) next).type == operator_types.EQUALS;
		next = i.take();
		assert (next instanceof record_token);
		result.variable_types = get_variable_declarations((record_token) next);
		custom_types.put(result.name, result);
	}

	private void get_arguments_for_declaration(grouper_token i,
			boolean is_procedure, List<String> names, List<pascal_type> types,
			List<Boolean> are_varargs) { // need
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
					are_varargs.add(is_varargs);
					names.add(((word_token) next).name);
					j++;
				} while ((next = arguments_token.take()) instanceof comma_token);

				assert (next instanceof colon_token);
				pascal_type type;
				try {
					type = get_next_java_class(arguments_token);
					while (j > 0) {
						types.add(type);
						j--;
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private List<variable_declaration> get_variable_declarations(grouper_token i) {
		List<variable_declaration> result = new ArrayList<variable_declaration>();
		/*
		 * reusing it, so it is further out of scope than necessary
		 */
		List<String> names = new ArrayList<String>(1);
		token next;
		do {
			do {
				names.add(get_word_value(i));
				next = i.take();
			} while (next instanceof comma_token);
			assert (next instanceof colon_token);
			pascal_type type;
			try {
				type = get_next_java_class(i);
				assert_next_semicolon(i);
				for (String s : names) {
					result.add(new variable_declaration(s, type));
					/*
					 * TODO make sure this conforms to pascal
					 */
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			names.clear(); // reusing the linked list object
			next = i.peek();
		} while (next instanceof word_token);
		return result;
	}

	private List<executable> get_function_body(begin_end_token t) {
		List<executable> commands = new ArrayList<executable>();
		while (t.hasNext()) {
			commands.add(get_next_command(t));
		}
		return commands;
	}

	executable get_next_command(grouper_token token_iterator) {
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
			return new if_statement(condition, command, else_command);
		} else if (next instanceof while_token) {
			returns_value condition = get_next_returns_value(token_iterator);
			next = token_iterator.take();
			assert (next instanceof do_token);
			executable command = get_next_command(token_iterator);
			return new while_statement(condition, command);
		} else if (next instanceof begin_end_token) {
			instruction_grouper begin_end_preprocessed = new instruction_grouper();
			begin_end_token cast_token = (begin_end_token) next;
			while (cast_token.hasNext()) {
				begin_end_preprocessed
						.add_command(get_next_command(cast_token));
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
			if (downto) {
				return new downto_for_statement(tmp_var, first_value,
						last_value, get_next_command(token_iterator));
			} else {
				return new for_statement(tmp_var, first_value, last_value,
						get_next_command(token_iterator));
			}
		} else if (next instanceof repeat_token) {
			executable command = get_next_command(token_iterator);
			returns_value condition = get_next_returns_value(token_iterator);
			return new repeat_instruction(command, condition);
		} else if (next instanceof word_token) {
			String name = get_word_value(next);
			next = token_iterator.peek_no_EOF();
			if (next instanceof parenthesized_token) {
				token_iterator.take();
				returns_value[] arguments = get_arguments_for_call((parenthesized_token) next);
				assert_next_semicolon(token_iterator);
				return new abstract_function_call(name, arguments);
			} else {
				// at this point assuming it is a variable identifier.
				variable_identifier identifier = get_next_var_identifier(name,
						token_iterator);
				next = token_iterator.take();
				assert (next instanceof assignment_token);
				returns_value value_to_assign = get_next_returns_value(token_iterator);
				assert_next_semicolon(token_iterator);
				return new variable_set(identifier, value_to_assign);
			}
		}
		System.err
				.println("Could not identify token "
						+ next
						+ ", because it did not match any of the criteria for an instruction");
		return null;
	}

	void assert_next_semicolon(grouper_token i) {
		token next = i.take();
		assert (next instanceof semicolon_token);
	}

	variable_identifier get_next_var_identifier(grouper_token i) {
		token next;
		variable_identifier identifier = new variable_identifier();
		identifier.add(new string_subvar_identifier(get_word_value(i)));
		while (true) {
			if (i.peek() instanceof period_token) {
				i.take();
				next = i.take();
				if (next instanceof word_token) {
					identifier.add(new string_subvar_identifier(
							((word_token) next).name));
				} else {
					error();
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
					error();
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

	returns_value get_single_value(parenthesized_token t) {
		returns_value result = get_next_returns_value(t);
		assert (!t.hasNext());
		return result;
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
			result = new constant_access(((string_token) next).value);
		} else if (next instanceof word_token) {
			String name = ((word_token) next).name;
			if (!((next = iterator.peek()) instanceof EOF_token)) {
				if (next instanceof parenthesized_token) {
					return new abstract_function_call(name,
							get_arguments_for_call((parenthesized_token) next));
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

	String get_word_value(token t) {
		assert (t instanceof word_token);
		return ((word_token) t).name;
	}

	String get_word_value(grouper_token i) {
		return get_word_value(i.take());
	}

	pascal_type get_basic_type(String s) throws ClassNotFoundException {
		s = s.intern();
		if (s == "integer") {
			return pascal_type.Integer;
		}
		if (s == "string") {
			return pascal_type.String;
		}
		if (s == "float") {
			return pascal_type.Float;
		}
		if (s == "real") {
			return pascal_type.Double;
		}
		if (s == "long") {
			return pascal_type.Long;
		}
		if (s == "boolean") {
			return pascal_type.Boolean;
		}
		// TODO add more types
		return new class_pascal_type(Class.forName("plugins." + s));
	}

	pascal_type get_next_java_class(grouper_token i)
			throws ClassNotFoundException {
		String s = get_word_value(i.peek_no_EOF());
		if (s == "array") {
			ArrayList<Integer> lower = new ArrayList<Integer>(1);
			ArrayList<Integer> upper = new ArrayList<Integer>(1);
			while (s == "array") {
				i.take();
				bracketed_token bounds = (bracketed_token) i.take();
				lower.add(((integer_token) bounds.take()).value);
				token next = bounds.take();
				assert (next instanceof period_token);
				next = bounds.take();
				assert (next instanceof period_token);
				upper.add(((integer_token) bounds.take()).value);
				next = i.take();
				assert (next instanceof of_token);
				s = get_word_value(i.peek_no_EOF());
			}
			i.take();
			pascal_type element_class = get_basic_type(s);
			int[] lowerarray = new int[lower.size()];
			int[] sizes = new int[upper.size()];
			for (int j = 0; j < lower.size(); j++) {
				lowerarray[j] = lower.get(j);
				sizes[j] = upper.get(j) - lowerarray[j] + 1;
			}
			return new array_type(element_class, lowerarray, sizes);
		}
		i.take();
		return get_basic_type(s);
	}

	void error() {
		System.err
				.println("problem with your code that the interpreter has not been taught to identify");
		System.exit(0);
	}
}