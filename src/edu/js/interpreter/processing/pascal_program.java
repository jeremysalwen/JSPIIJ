package edu.js.interpreter.processing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multiset.Entry;

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
import edu.js.interpreter.preprocessed.instructions.returns_value.abstract_function_call;
import edu.js.interpreter.preprocessed.instructions.returns_value.returns_value;
import edu.js.interpreter.preprocessed.interpreting_objects.function_on_stack;
import edu.js.interpreter.tokens.token;
import edu.js.interpreter.tokens.basic.colon_token;
import edu.js.interpreter.tokens.basic.comma_token;
import edu.js.interpreter.tokens.basic.function_token;
import edu.js.interpreter.tokens.basic.of_token;
import edu.js.interpreter.tokens.basic.period_token;
import edu.js.interpreter.tokens.basic.procedure_token;
import edu.js.interpreter.tokens.basic.program_token;
import edu.js.interpreter.tokens.basic.semicolon_token;
import edu.js.interpreter.tokens.basic.var_token;
import edu.js.interpreter.tokens.grouping.base_grouper_token;
import edu.js.interpreter.tokens.grouping.begin_end_token;
import edu.js.interpreter.tokens.grouping.bracketed_token;
import edu.js.interpreter.tokens.grouping.grouper_token;
import edu.js.interpreter.tokens.grouping.record_token;
import edu.js.interpreter.tokens.grouping.type_token;
import edu.js.interpreter.tokens.value.integer_token;
import edu.js.interpreter.tokens.value.operator_token;
import edu.js.interpreter.tokens.value.operator_types;
import edu.js.interpreter.tokens.value.word_token;

public class pascal_program implements Runnable {
	public run_mode mode;

	public function_declaration main;

	public function_on_stack main_running;

	public HashMap<String, custom_type_declaration> custom_types;

	public HashMap<String, pascal_type> typedefs;

	public custom_type_generator type_generator;

	public String program_name;

	/*
	 * plugins and functions
	 */
	private ListMultimap<String, abstract_function> callable_functions;

	public void run() {
		mode = run_mode.running;
		main_running = new function_on_stack(this, main, new Object[0]);
		main_running.execute();

	}

	pascal_program(List<plugin_declaration> plugins,
			custom_type_generator type_generator) {
		callable_functions = ArrayListMultimap.create();
		custom_types = new HashMap<String, custom_type_declaration>();
		typedefs=new HashMap<String, pascal_type>();
		for (plugin_declaration p : plugins) {
			add_callable_function(p);
		}
		this.type_generator = type_generator;
		main = new function_declaration(this);
		main.are_varargs = new boolean[0];
		main.argument_names = new String[0];
		main.argument_types = new pascal_type[0];
		main.name = "main";
	}

	public pascal_program(String program, List<plugin_declaration> plugins,
			custom_type_generator type_generator) {
		this(plugins, type_generator);
		Grouper grouper = new Grouper(program);
		new Thread(grouper).start();
		parse_tree(grouper.token_queue);
	}

	public void parse_tree(base_grouper_token tokens) {
		while (tokens.hasNext()) {
			add_next_declaration(tokens);
		}
	}

	private void add_next_declaration(base_grouper_token i) {
		token next = i.peek();
		if (next instanceof procedure_token || next instanceof function_token) {
			i.take();
			boolean is_procedure = next instanceof procedure_token;
			function_declaration declaration = new function_declaration(this,
					i, is_procedure);
			add_callable_function(declaration);
		} else if (next instanceof type_token) {
			i.take();
			add_custom_type_declaration(i);
		} else if (next instanceof begin_end_token) {
			main.instructions = main.get_next_command(i);
			next = i.take();
			assert (next instanceof period_token);
		} else if (next instanceof var_token) {
			i.take();
			List<variable_declaration> global_var_decs = get_variable_declarations(i);
			for (variable_declaration v : global_var_decs) {
				main.local_variables.add(v);
			}
		} else if (next instanceof program_token) {
			i.take();
			this.program_name = get_word_value(i);
			assert_next_semicolon(i);
		} else {

			i.take();
			System.err.println("Unexpected token outside of any construct: "
					+ next);
		}
	}

	public void add_callable_function(abstract_function f) {
		callable_functions.put(f.name().toLowerCase(), f);
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
		type_generator.output_class(result);
	}

	public List<variable_declaration> get_variable_declarations(grouper_token i) {
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
				type = get_next_pascal_type(i);
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

	public void assert_next_semicolon(grouper_token i) {
		token next = i.take();
		assert (next instanceof semicolon_token);
	}

	public String get_word_value(token t) {
		assert (t instanceof word_token);
		return ((word_token) t).name;
	}

	public String get_word_value(grouper_token i) {
		return get_word_value(i.take());
	}

	pascal_type get_basic_type(String s) throws ClassNotFoundException {
		s = s.toLowerCase().intern();
		if (s == "integer") {
			return class_pascal_type.Integer;
		}
		if (s == "string") {
			return class_pascal_type.StringBuilder;
		}
		if (s == "single" || s == "extended" || s == "double") {
			return class_pascal_type.Double;
		}
		if (s == "long") {
			return class_pascal_type.Long;
		}
		if (s == "boolean") {
			return class_pascal_type.Boolean;
		}
		if (s == "character") {
			return class_pascal_type.Character;
		}
		pascal_type type = typedefs.get(s);
		if (type != null) {
			return type;
		} else {
			return class_pascal_type.anew(Class
					.forName("edu.js.interpreter.custom_types."
							+ Integer.toHexString(custom_types.get(s)
									.hashCode())));
		}
	}

	public pascal_type get_next_pascal_type(grouper_token i)
			throws ClassNotFoundException {
		String s = get_word_value(i.peek_no_EOF());
		if (s == "array") {
			ArrayList<Integer> lower = new ArrayList<Integer>(1);
			ArrayList<Integer> upper = new ArrayList<Integer>(1);
			while (s == "array") {
				i.take();
				token next = i.take();
				if (next instanceof bracketed_token) {
					bracketed_token bounds = (bracketed_token) next;
					lower.add(((integer_token) bounds.take()).value);
					next = bounds.take();
					assert (next instanceof period_token);
					next = bounds.take();
					assert (next instanceof period_token);

					upper.add(((integer_token) bounds.take()).value);
					next = i.take();
				} else {
					lower.add(0);
					upper.add(-1); // So it is a zero length array
				}
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

	public void error() {
		System.err
				.println("problem with your code that the interpreter has not been taught to identify");
		System.exit(0);
	}

	public abstract_function_call generate_function_call(String name,
			returns_value[] args, function_declaration f) {
		List<abstract_function> possibilities = callable_functions.get(name
				.toLowerCase());
		returns_value[] converted;
		for (abstract_function a : possibilities) {
			converted = a.format_args(args, f);
			if (converted != null) {
				return new abstract_function_call(a, converted);
			}
		}
		System.err.println("Could not find function call " + name
				+ Arrays.toString(args));
		return null;
	}
}
