package processing;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;

import pascal_types.custom_type_declaration;
import preprocessed.Grouper;
import preprocessed.function_declaration;
import preprocessed.function_header;
import preprocessed.variable_declaration;
import preprocessed.instructions.executable;
import preprocessed.instructions.if_statement;
import preprocessed.instructions.instruction_grouper;
import preprocessed.instructions.variable_set;
import preprocessed.instructions.while_statement;
import preprocessed.instructions.returns_value.binary_operator_evaluation;
import preprocessed.instructions.returns_value.constant_access;
import preprocessed.instructions.returns_value.function_call;
import preprocessed.instructions.returns_value.plugin_call;
import preprocessed.instructions.returns_value.returns_value;
import preprocessed.instructions.returns_value.unary_operator_evaluation;
import preprocessed.instructions.returns_value.variable_access;
import preprocessed.interpreting_objects.variables.variable_identifier;
import tokens.assignment_token;
import tokens.begin_end_token;
import tokens.colon_token;
import tokens.comma_token;
import tokens.do_token;
import tokens.double_token;
import tokens.if_token;
import tokens.integer_token;
import tokens.operator_token;
import tokens.operator_types;
import tokens.parenthesized_token;
import tokens.period_token;
import tokens.semicolon_token;
import tokens.string_token;
import tokens.then_token;
import tokens.token;
import tokens.while_token;
import tokens.word_token;
import exceptions.grouping_exception;

public class pascal_program {
	public HashMap<String, custom_type_declaration> custom_types;
	public HashMap<function_header, function_declaration> functions;
	public HashMap<String, Class<pascalPlugin>> plugins;

	public static void main(String[] args) throws grouping_exception {
		System.out
				.println(new pascal_program()
						.get_next_command(new Grouper(
								"if x=5 then begin x:=7; y:=getstuff(5, x, 2 + 1 - y); end").tokens
								.listIterator()));
	}

	public pascal_program() {
		plugins = new HashMap<String, Class<pascalPlugin>>();
		loadPlugins();
	}

	public pascal_program(LinkedList<token> tokens) {
		ListIterator<token> token_iterator = tokens.listIterator();
		while (token_iterator.hasNext()) {
			// TODO lol
		}
	}

	private custom_type_declaration get_custom_type_declaration(
			ListIterator<token> i) {
		String tmp = get_word_value(i);
		assert (tmp.equals("type"));
		while (true) {

			String name = get_word_value(i);
			token next = i.next();
			assert (next instanceof operator_token);
			assert ((operator_token) next).type == operator_types.EQUALS;
			tmp = get_word_value(i);
			assert (tmp.equals("record"));
			while (true) {
				LinkedList<variable_declaration> variable_declarations = new LinkedList<variable_declaration>();
				String next_name = get_word_value(i);
				next = i.next();
				assert (next instanceof colon_token);
				variable_declarations.add(new variable_declaration(next_name,
						get_java_type(get_word_value(i))));
			}

		}
	}

	private function_declaration get_function_declaration(ListIterator<token> t) {
		function_declaration unfinished_function = new function_declaration();
		token next;
		String function_type = get_word_value(t);
		boolean is_procedure = false;
		if (function_type.equals("procedure")) {
			is_procedure = true;
		} else if (function_type.equals("function")) {
			is_procedure = false;
		} else {
			System.err
					.println("unknown function type passed to get_function_declaration");
		}
		String name = ((word_token) t.next()).name;
		LinkedList<variable_declaration> arguments = new LinkedList<variable_declaration>();
		next = t.next();
		if (next instanceof parenthesized_token) {
			parenthesized_token arguments_token = (parenthesized_token) next;
			ListIterator<token> argument_iterator = arguments_token.insides
					.listIterator();
			while (argument_iterator.hasNext()) {
				LinkedList<String> names = new LinkedList<String>();
				names.add(((word_token) argument_iterator.next()).name);
				next = argument_iterator.next();
				while (next instanceof comma_token) {
					names.add(((word_token) argument_iterator.next()).name);
					next = argument_iterator.next();
				}
				next = argument_iterator.next();
				assert (next instanceof colon_token);
				Object type = get_java_type(get_word_value(argument_iterator));
				for (String s : names) {
					arguments.add(new variable_declaration(s, type));
				}
			}
		}
		next = t.next();
		Object return_type = null;
		assert (is_procedure ^ next instanceof colon_token);
		if (!is_procedure && next instanceof colon_token) {
			return_type = get_java_type(get_word_value(t));
		}
		assert_next_semicolon(t);
		next = t.next();
		assert (next instanceof begin_end_token);
		begin_end_token body = (begin_end_token) next;
		ListIterator<token> body_iterator = body.insides.listIterator();
		LinkedList<executable> commands = new LinkedList<executable>();
		while (body_iterator.hasNext()) {
			commands.add(get_next_command(body_iterator));
		}
		unfinished_function.instructions = commands;
		unfinished_function.header = new function_header(name, arguments,
				return_type);
		return unfinished_function;
	}

	executable get_next_command(ListIterator<token> token_iterator) {
		token next = token_iterator.next();
		if (next instanceof if_token) {
			returns_value condition = get_next_returns_value(token_iterator);
			next = token_iterator.next();
			assert (next instanceof then_token);
			executable command = get_next_command(token_iterator);
			return new if_statement(condition, command);
		} else if (next instanceof while_token) {
			returns_value condition = get_next_returns_value(token_iterator);
			next = token_iterator.next();
			assert (next instanceof do_token);
			executable command = get_next_command(token_iterator);
			return new while_statement(condition, command);
		} else if (next instanceof begin_end_token) {
			instruction_grouper begin_end_preprocessed = new instruction_grouper();
			ListIterator<token> begin_end_iterator = ((begin_end_token) next).insides
					.listIterator();
			while (begin_end_iterator.hasNext()) {
				begin_end_preprocessed
						.add_command(get_next_command(begin_end_iterator));
			}
			return begin_end_preprocessed;
		} else if (next instanceof word_token) {

			String name = get_word_value(next);
			next = token_iterator.next();
			if (next instanceof parenthesized_token) {
				LinkedList<returns_value> arguments = get_arguments_for_call((parenthesized_token) next);
				assert_next_semicolon(token_iterator);
				if (plugins.containsKey(name)) {
					return new plugin_call(name, arguments);
				} else {
					return new function_call(name, arguments);
				}
			} else {
				// at this point assuming it is a variable identifier.
				token_iterator.previous();
				token_iterator.previous();
				variable_identifier identifier = get_next_var_identifier(token_iterator);
				next = token_iterator.next();
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

	void assert_next_semicolon(ListIterator<token> i) {
		token next = i.next();
		assert (next instanceof semicolon_token);
	}

	variable_identifier get_next_var_identifier(ListIterator<token> i) {
		token next = i.next();
		variable_identifier identifier = new variable_identifier();
		identifier.add(((word_token) next).name);
		while (i.hasNext() && (next = i.next()) instanceof period_token) {
			next = i.next();
			assert (next instanceof word_token);
			identifier.add(((word_token) next).name);
		}
		i.previous();
		return identifier;
	}

	returns_value get_single_value(parenthesized_token t) {
		ListIterator<token> iterator = t.insides.listIterator();
		returns_value result = get_next_returns_value(iterator);
		assert (!iterator.hasNext());
		return result;
	}

	LinkedList<returns_value> get_arguments_for_call(parenthesized_token t) {
		ListIterator<token> iterator = t.insides.listIterator();
		LinkedList<returns_value> result = new LinkedList<returns_value>();
		while (iterator.hasNext()) {
			result.add(get_next_returns_value(iterator));
			token next = iterator.next();
			assert (next instanceof comma_token);
		}
		return result;
	}

	returns_value get_next_returns_value(ListIterator<token> iterator) {
		token next = iterator.next();
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
			if (iterator.hasNext()) {
				next = iterator.next();
				if (next instanceof parenthesized_token) {
					LinkedList<returns_value> arguments = get_arguments_for_call((parenthesized_token) next);
					if (plugins.containsKey(name)) {
						result = new plugin_call(name, arguments);
					} else {
						result = new function_call(name, arguments);
					}
				} else {
					iterator.previous();
					iterator.previous();
					result = new variable_access(
							get_next_var_identifier(iterator));
				}
			} else {
				iterator.previous();
				result = new variable_access(get_next_var_identifier(iterator));
			}
		}
		assert (result != null);
		if (iterator.hasNext()) {
			next = iterator.next();
			if (next instanceof operator_token) {
				result = new binary_operator_evaluation(result,
						get_next_returns_value(iterator),
						((operator_token) next).type);
			} else {
				iterator.previous();
			}
		}
		return result;
	}

	String get_word_value(token t) {
		assert (t instanceof word_token);
		return ((word_token) t).name;
	}

	String get_word_value(ListIterator<token> i) {
		return get_word_value(i.next());
	}

	private void loadPlugins() {
		File pluginFolder = new File("plugins/");
		File[] pluginarray = pluginFolder.listFiles(new java.io.FileFilter() {
			public boolean accept(File pathname) {
				return pathname.getName().endsWith(".class");
			}
		});
		ArrayList<Class> classes = new ArrayList<Class>();
		ClassLoader classloader = ClassLoader.getSystemClassLoader();
		for (File f : pluginarray)
			try {
				String name = f.getName().substring(0,
						f.getName().indexOf(".class"));
				Class<?> plugin_class = classloader
						.loadClass("plugins." + name);
				if (plugin_class.isAssignableFrom(pascalPlugin.class)) {
					classes.add(plugin_class);
				}
			} catch (ClassNotFoundException ex) {
				ex.printStackTrace();
			}
		for (Class c : classes) {
			plugins.put(c.getSimpleName(), c);
			System.out.println(c.getName());
		}
	}

	public Object get_java_type(String name) {
		name = name.intern();
		if (name == "integer") {
			return Integer.class;
		}
		if (name == "string") {
			return String.class;
		}
		if (name == "float") {
			return Double.class;
		}
		if (name == "boolean") {
			return Boolean.class;
		}
		return custom_types.get(name);
	}
}
