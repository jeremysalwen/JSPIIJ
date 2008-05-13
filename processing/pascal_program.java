package processing;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.TreeMap;

import pascalTypes.pascalType;
import preprocessed.executable;
import preprocessed.function_call;
import preprocessed.function_declaration;
import preprocessed.function_header;
import preprocessed.if_statement;
import preprocessed.plugin_call;
import preprocessed.returns_value;
import preprocessed.variable_declaration;
import preprocessed.variable_set;
import preprocessed.while_statement;
import sun.org.mozilla.javascript.internal.Function;
import tokens.assignment_token;
import tokens.begin_end_token;
import tokens.colon_token;
import tokens.comma_token;
import tokens.parenthesized_token;
import tokens.semicolon_token;
import tokens.token;
import tokens.word_token;

public class pascal_program {
	public HashMap<function_header, function_declaration> functions;
	public TreeMap<String, Class<pascalPlugin<pascalType>>> plugins;

	public static void main(String[] args) {
		new pascal_program(null);
	}

	public pascal_program(LinkedList<token> tokens) {
		ListIterator<token> token_iterator=tokens.listIterator();
		while(token_iterator.hasNext()) {
			token_iterator.
		}
	}

	private function_declaration get_function_declaration(ListIterator<token> t) {
		function_declaration unfinished_function=new function_declaration();
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
				assert (argument_iterator.next() instanceof colon_token);
				Class type = pascalType
						.get_java_type(get_word_value(argument_iterator));
				for (String s : names) {
					arguments.add(new variable_declaration(s, type));
				}
			}
		}
		next = t.next();
		Class return_type = null;
		if (next instanceof colon_token) {
			return_type = pascalType.get_java_type(get_word_value(t));
		}
		assert (t.next() instanceof semicolon_token);
		next = t.next();
		assert (next instanceof begin_end_token);
		begin_end_token body = (begin_end_token) next;
		ListIterator<token> body_iterator = body.insides.listIterator();
		LinkedList<executable> commands = new LinkedList<executable>();
		while (body_iterator.hasNext()) {
			commands.add(get_next_command(body_iterator));
		}
	}

	executable get_next_command(ListIterator<token> token_iterator) {
		token next = token_iterator.next();
		if(next instanceof i)
		if (next instanceof word_token) {
			String name = get_word_value(next);
			next = token_iterator.next();
			if (next instanceof parenthesized_token) {
				if (name.equals("if")) {
					returns_value<Boolean> condition = get_next_returns_value(((parenthesized_token) next).insides
							.listIterator());
					next = token_iterator.next();
					assert (next instanceof word_token);
					assert ((word_token) next).name.equals("then");
					executable command = get_next_command(token_iterator);
					return new if_statement(condition, command);
				} else if (name.equals("while")) {
					returns_value<Boolean> condition = get_next_returns_value(((parenthesized_token) next).insides
							.listIterator());
					assert (get_word_value(token_iterator).equals("do"));
					executable command = get_next_command(token_iterator);
					return new while_statement(condition, command);
				}
				LinkedList<returns_value> arguments = get_arguments_for_call((parenthesized_token) next);
				assert (token_iterator.next() instanceof semicolon_token);
				if (plugins.containsKey(name)) {
					return new plugin_call(name, arguments);
				} else {
					return new function_call(name, arguments);
				}
			} else if (next instanceof assignment_token) {
				returns_value value_to_assign = get_next_returns_value(token_iterator);
				assert_next_semicolon(token_iterator);
				return new variable_set(name, value_to_assign);
			}

		}
	}

	void assert_next_semicolon(ListIterator<token> i) {
		assert (i.next() instanceof semicolon_token);
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
			assert (iterator.next() instanceof comma_token);
		}
		return result;
	}

	returns_value get_next_returns_value(ListIterator<token> iterator) {

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
		ClassLoader c = ClassLoader.getSystemClassLoader();
		for (File f : pluginarray)
			try {
				String name = f.getName().substring(0,
						f.getName().indexOf(".class"));
				classes.add(c.loadClass("plugins." + name));
			} catch (ClassNotFoundException ex) {
				ex.printStackTrace();
			}
		for (Class c2 : classes)
			if (!StaticMethods.isSubClass(c2, pascalPlugin.class))
				classes.remove(c2);
		for (Class c3 : classes) {
			plugins.put(c3.getSimpleName(), c3);
			System.out.println(c3.getName());
		}
	}
}
