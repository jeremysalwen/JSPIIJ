package processing;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pascal_types.custom_type_declaration;
import preprocessed.Grouper;
import preprocessed.abstract_function;
import preprocessed.function_declaration;
import preprocessed.plugin_declaration;
import preprocessed.variable_declaration;
import preprocessed.instructions.downto_for_statement;
import preprocessed.instructions.executable;
import preprocessed.instructions.for_statement;
import preprocessed.instructions.if_statement;
import preprocessed.instructions.instruction_grouper;
import preprocessed.instructions.repeat_instruction;
import preprocessed.instructions.variable_set;
import preprocessed.instructions.while_statement;
import preprocessed.instructions.returns_value.abstract_function_call;
import preprocessed.instructions.returns_value.binary_operator_evaluation;
import preprocessed.instructions.returns_value.constant_access;
import preprocessed.instructions.returns_value.returns_value;
import preprocessed.instructions.returns_value.unary_operator_evaluation;
import preprocessed.instructions.returns_value.variable_access;
import preprocessed.interpreting_objects.variables.variable_identifier;
import tokens.EOF_token;
import tokens.token;
import tokens.basic.assignment_token;
import tokens.basic.colon_token;
import tokens.basic.comma_token;
import tokens.basic.do_token;
import tokens.basic.downto_token;
import tokens.basic.else_token;
import tokens.basic.for_token;
import tokens.basic.function_token;
import tokens.basic.if_token;
import tokens.basic.period_token;
import tokens.basic.procedure_token;
import tokens.basic.repeat_token;
import tokens.basic.semicolon_token;
import tokens.basic.then_token;
import tokens.basic.to_token;
import tokens.basic.var_token;
import tokens.basic.while_token;
import tokens.grouping.base_grouper_token;
import tokens.grouping.begin_end_token;
import tokens.grouping.grouper_token;
import tokens.grouping.parenthesized_token;
import tokens.grouping.record_token;
import tokens.grouping.type_token;
import tokens.value.double_token;
import tokens.value.integer_token;
import tokens.value.operator_token;
import tokens.value.operator_types;
import tokens.value.string_token;
import tokens.value.word_token;
import exceptions.grouping_exception;

public class pascal_program {
	function_declaration main;

	public HashMap<String, custom_type_declaration> custom_types;

	/*
	 * plugins and functions
	 */
	public HashMap<abstract_function, abstract_function> callable_functions;

	public HashMap<String, Object> global_variables;

	public static void main(String[] args) throws grouping_exception {
		new pascal_program("var x:integer;" + " begin" + " x:=2+5;"
				+ " if((x mod 7)=0) then writeln('hi there'+returnfive(4));"
				+ " end;" + " function returnfive(x:integer): integer;"
				+ " begin " + "result:=5;" + " end;").run();
	}

	public void run() {
		main.call(this, new Object[0]);
	}

	public pascal_program() {
		callable_functions = new HashMap<abstract_function, abstract_function>();
		loadPlugins();
		main = new function_declaration();
		main.name = "main";
		main.argument_types = new ArrayList<Class>();
	}

	public pascal_program(String program) {
		this();
		Grouper grouper = new Grouper(program);
		new Thread(grouper).start();
		parse_tree(grouper.token_queue);
	}

	public void parse_tree(base_grouper_token tokens) {
		while (tokens.hasNext()) {
			add_next_declaration(tokens);
		}
	}

	public pascal_program(base_grouper_token tokens) {
		this();
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
					declaration.return_type = get_java_class(get_word_value(i));
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
			main.local_variables = get_variable_declarations(i);
			return;
		}
	}

	private void add_custom_type_declaration(grouper_token i) {
		custom_type_declaration result = new custom_type_declaration();
		String name = get_word_value(i);
		token next = i.take();
		assert (next instanceof operator_token);
		assert ((operator_token) next).type == operator_types.EQUALS;
		next = i.take();
		assert (next instanceof record_token);
		result.variable_types = get_variable_declarations((record_token) next);
		assert_next_semicolon(i);
		custom_types.put(name, result);
	}

	private void get_arguments_for_declaration(grouper_token i,
			boolean is_procedure, List<String> names, List<Class> types,
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
				Class type;
				try {
					type = get_java_class(get_word_value(arguments_token));
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
			Class type;
			try {
				type = get_java_class(get_word_value(i));
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
		while (i.peek() instanceof period_token) {
			i.take();
			next = i.take();
			assert (next instanceof word_token);
			identifier.add(((word_token) next).name);
		}
		return identifier;
	}

	variable_identifier get_next_var_identifier(String initial, grouper_token i) {
		token next;
		variable_identifier identifier = new variable_identifier();
		identifier.add(initial);
		while (i.peek() instanceof period_token) {
			i.take();
			next = i.take();
			assert (next instanceof word_token);
			identifier.add(((word_token) next).name);
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

	private void loadPlugins() {
		File pluginFolder = new File("plugins/");
		File[] pluginarray = pluginFolder.listFiles(new java.io.FileFilter() {
			public boolean accept(File pathname) {
				return pathname.getName().endsWith(".class");
			}
		});
		ClassLoader classloader = ClassLoader.getSystemClassLoader();
		for (File f : pluginarray) {
			try {
				String filename = f.getName();
				filename = filename.substring(0, filename.indexOf(".class"));
				Class c = classloader.loadClass("plugins." + filename);
				if (pascal_plugin.class.isAssignableFrom(c)) {
					for (Method m : c.getMethods()) {
						abstract_function tmp = new plugin_declaration(m);
						this.callable_functions.put(tmp, tmp);
					}
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

		}
	}

	Class get_java_class(String name) throws ClassNotFoundException {
		name = name.intern();
		if (name == "integer") {
			return Integer.class;
		}
		if (name == "string") {
			return String.class;
		}
		if (name == "float") {
			return Float.class;
		}
		// TODO add more types
		return Class.forName(name);
	}
}
