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
import preprocessed.instructions.executable;
import preprocessed.instructions.if_statement;
import preprocessed.instructions.instruction_grouper;
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
import tokens.assignment_token;
import tokens.base_grouper_token;
import tokens.begin_end_token;
import tokens.colon_token;
import tokens.comma_token;
import tokens.do_token;
import tokens.double_token;
import tokens.end_token;
import tokens.function_token;
import tokens.grouper_token;
import tokens.if_token;
import tokens.integer_token;
import tokens.operator_token;
import tokens.operator_types;
import tokens.parenthesized_token;
import tokens.period_token;
import tokens.procedure_token;
import tokens.record_token;
import tokens.semicolon_token;
import tokens.string_token;
import tokens.then_token;
import tokens.token;
import tokens.type_token;
import tokens.var_token;
import tokens.while_token;
import tokens.word_token;
import exceptions.grouping_exception;

public class pascal_program {
	function_declaration main;

	public HashMap<String, custom_type_declaration> custom_types;

	/*
	 * plugins and functions
	 */
	public HashMap<abstract_function, abstract_function> callable_functions;

	public static void main(String[] args) throws grouping_exception {
		new pascal_program(new Grouper("var x:integer;" + " begin" + " x:=2+5;"
				+ " if((x mod 7)=0) then writeln('hi there'+returnfive(4));"
				+ " end;" + " function returnfive(x:integer): integer;"
				+ " begin " + "result:=5;" + " end;").tokens).run();
	}

	public void run() {
		main.call(this, new ArrayList());
	}

	public pascal_program() {
		callable_functions = new HashMap<abstract_function, abstract_function>();
		loadPlugins();
		main = new function_declaration();
		main.name = "main";
		main.argument_types = new ArrayList<Class>();
	}

	public pascal_program(base_grouper_token tokens) {
		this();
		while (tokens.has_next()) {
			add_next_declaration(tokens);
		}
	}

	private void add_next_declaration(base_grouper_token i) {
		token next = i.get_next_token_no_EOF();
		if (next instanceof procedure_token || next instanceof function_token) {
			boolean is_procedure = next instanceof procedure_token;
			function_declaration declaration = new function_declaration();
			declaration.name = get_word_value(i);
			get_arguments_for_declaration(i, is_procedure,
					declaration.argument_names, declaration.argument_types);
			next = i.get_next_token_no_EOF();
			assert (is_procedure ^ next instanceof colon_token);
			if (!is_procedure && next instanceof colon_token) {
				try {
					declaration.return_type = Class.forName(get_word_value(i));
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
			assert_next_semicolon(i);
			List<variable_declaration> local_variables = null;
			next = i.get_next_token_no_EOF();
			if (next instanceof var_token) {
				local_variables = get_variable_declarations(i);
			} else {
				i.push_back(next);
			}
			List<executable> commands = get_function_body(i);

			declaration.instructions = commands;

			declaration.local_variables = local_variables == null ? new ArrayList<variable_declaration>()
					: local_variables;
			callable_functions.put(declaration, declaration);
			return;
		}
		if (next instanceof type_token) {
			add_custom_type_declaration(i);
			return;
		}
		if (next instanceof begin_end_token) {
			i.push_back(next);
			main.instructions = get_function_body(i);
			return;
		}
		if (next instanceof var_token) {
			main.local_variables = get_variable_declarations(i);
			return;
		}
	}

	private void add_custom_type_declaration(grouper_token i) {
		custom_type_declaration result = new custom_type_declaration();
		String name = get_word_value(i);
		token next = i.get_next_token_no_EOF();
		assert (next instanceof operator_token);
		assert ((operator_token) next).type == operator_types.EQUALS;
		next = i.get_next_token_no_EOF();
		assert (next instanceof record_token);
		result.variable_types = get_variable_declarations(i);
		next = i.get_next_token_no_EOF();
		assert (next instanceof end_token);
		assert_next_semicolon(i);
		custom_types.put(name, result);
	}

	private void get_arguments_for_declaration(grouper_token i,
			boolean is_procedure, List<String> names, List<Class> types) { // need
		// to
		// get
		// rid
		// of
		// tiss
		// and
		// function_header
		// class
		token next = i.get_next_token_no_EOF();
		if (next instanceof parenthesized_token) {
			parenthesized_token arguments_token = (parenthesized_token) next;
			next = arguments_token.get_next_token();
			while (!(next instanceof EOF_token)) {
				names.add(((word_token) next).name);
				next = arguments_token.get_next_token();
				int j = 0; // counts number added of this type
				while (next instanceof comma_token) {
					names
							.add(((word_token) arguments_token.get_next_token()).name);
					next = arguments_token.get_next_token();
					j++;
				}
				assert (next instanceof colon_token);
				Class type;
				try {
					type = Class.forName(get_word_value(arguments_token));
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
			next = i.get_next_token_no_EOF();
			do {
				names.add(get_word_value(i));
				next = i.get_next_token_no_EOF();
			} while (next instanceof comma_token);
			next = i.get_next_token_no_EOF();
			assert (next instanceof colon_token);
			Class type;
			try {
				type = Class.forName(get_word_value(i));
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
			next = i.peek_next_token();
		} while (next instanceof word_token);
		return result;
	}

	private List<executable> get_function_body(grouper_token t) {
		token next = t.get_next_token_no_EOF();
		assert (next instanceof begin_end_token);
		begin_end_token body = (begin_end_token) next;
		List<executable> commands = new ArrayList<executable>();
		while (body.has_next()) {
			commands.add(get_next_command(body));
		}
		return commands;
	}

	executable get_next_command(grouper_token token_iterator) {
		token next = token_iterator.get_next_token_no_EOF();
		if (next instanceof if_token) {
			returns_value condition = get_next_returns_value(token_iterator);
			next = token_iterator.get_next_token_no_EOF();
			assert (next instanceof then_token);
			executable command = get_next_command(token_iterator);
			return new if_statement(condition, command);
		} else if (next instanceof while_token) {
			returns_value condition = get_next_returns_value(token_iterator);
			next = token_iterator.get_next_token_no_EOF();
			assert (next instanceof do_token);
			executable command = get_next_command(token_iterator);
			return new while_statement(condition, command);
		} else if (next instanceof begin_end_token) {
			instruction_grouper begin_end_preprocessed = new instruction_grouper();
			begin_end_token cast_token = (begin_end_token) next;
			while (cast_token.has_next()) {
				begin_end_preprocessed
						.add_command(get_next_command(cast_token));
			}
			return begin_end_preprocessed;
		} else if (next instanceof word_token) {

			String name = get_word_value(next);
			next = token_iterator.peek_next_token_no_EOF();
			if (next instanceof parenthesized_token) {
				token_iterator.get_next_token_no_EOF();
				returns_value[] arguments = get_arguments_for_call((parenthesized_token) next);
				assert_next_semicolon(token_iterator);
				return new abstract_function_call(name, arguments);
			} else {
				// at this point assuming it is a variable identifier.
				variable_identifier identifier = get_next_var_identifier(name,
						token_iterator);
				next = token_iterator.get_next_token_no_EOF();
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
		token next = i.get_next_token_no_EOF();
		assert (next instanceof semicolon_token);
	}

	variable_identifier get_next_var_identifier(String initial, grouper_token i) {
		token next = i.get_next_token_no_EOF();
		variable_identifier identifier = new variable_identifier();
		identifier.add(initial);
		while (i.peek_next_token() instanceof period_token) {
			i.get_next_token();
			next = i.get_next_token_no_EOF();
			assert (next instanceof word_token);
			identifier.add(((word_token) next).name);
		}
		return identifier;
	}

	returns_value get_single_value(parenthesized_token t) {
		returns_value result = get_next_returns_value(t);
		assert (!t.has_next());
		return result;
	}

	returns_value[] get_arguments_for_call(parenthesized_token t) {
		List<returns_value> result = new ArrayList<returns_value>();
		while (t.has_next()) {
			result.add(get_next_returns_value(t));
			if (t.has_next()) {
				token next = t.get_next_token_no_EOF();
				assert (next instanceof comma_token);
			}
		}
		return result.toArray(new returns_value[result.size()]);
	}

	returns_value get_next_returns_value(grouper_token iterator) {
		token next = iterator.get_next_token_no_EOF();
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
			if (!((next = iterator.peek_next_token()) instanceof EOF_token)) {
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
		if ((next = iterator.peek_next_token()) instanceof operator_token) {
			iterator.get_next_token_no_EOF();
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
		return get_word_value(i.get_next_token_no_EOF());
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
				Class c = classloader.loadClass("plugins." + f.getName());
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
}
