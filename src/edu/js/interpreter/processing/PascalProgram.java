package edu.js.interpreter.processing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import edu.js.interpreter.pascaltypes.ArrayType;
import edu.js.interpreter.pascaltypes.CustomType;
import edu.js.interpreter.pascaltypes.JavaClassBasedType;
import edu.js.interpreter.pascaltypes.PascalType;
import edu.js.interpreter.preprocessed.Grouper;
import edu.js.interpreter.preprocessed.AbstractFunction;
import edu.js.interpreter.preprocessed.CustomTypeGenerator;
import edu.js.interpreter.preprocessed.FunctionDeclaration;
import edu.js.interpreter.preprocessed.PluginDeclaration;
import edu.js.interpreter.preprocessed.VariableDeclaration;
import edu.js.interpreter.preprocessed.instructions.returnsvalue.AbstractFunctionCall;
import edu.js.interpreter.preprocessed.instructions.returnsvalue.ReturnsValue;
import edu.js.interpreter.preprocessed.interpretingobjects.FunctionOnStack;
import edu.js.interpreter.tokens.Token;
import edu.js.interpreter.tokens.basic.ColonToken;
import edu.js.interpreter.tokens.basic.CommaToken;
import edu.js.interpreter.tokens.basic.FunctionToken;
import edu.js.interpreter.tokens.basic.OfToken;
import edu.js.interpreter.tokens.basic.PeriodToken;
import edu.js.interpreter.tokens.basic.ProcedureToken;
import edu.js.interpreter.tokens.basic.ProgramToken;
import edu.js.interpreter.tokens.basic.SemicolonToken;
import edu.js.interpreter.tokens.basic.VarToken;
import edu.js.interpreter.tokens.grouping.BaseGrouperToken;
import edu.js.interpreter.tokens.grouping.BeginEndToken;
import edu.js.interpreter.tokens.grouping.BracketedToken;
import edu.js.interpreter.tokens.grouping.GrouperToken;
import edu.js.interpreter.tokens.grouping.RecordToken;
import edu.js.interpreter.tokens.grouping.TypeToken;
import edu.js.interpreter.tokens.value.IntegerToken;
import edu.js.interpreter.tokens.value.OperatorToken;
import edu.js.interpreter.tokens.value.OperatorTypes;
import edu.js.interpreter.tokens.value.WordToken;

public class PascalProgram implements Runnable {
	public volatile RunMode mode;

	public FunctionDeclaration main;

	public FunctionOnStack main_running;

	public HashMap<String, CustomType> custom_types;

	public HashMap<String, PascalType> typedefs;

	public CustomTypeGenerator type_generator;

	public String program_name;

	/*
	 * plugins and functions
	 */
	private ListMultimap<String, AbstractFunction> callable_functions;

	public void run() {
		mode = RunMode.running;
		main_running = new FunctionOnStack(this, main, new Object[0]);
		main_running.execute();

	}

	PascalProgram(List<PluginDeclaration> plugins,
			CustomTypeGenerator type_generator) {
		callable_functions = ArrayListMultimap.create();
		custom_types = new HashMap<String, CustomType>();
		typedefs=new HashMap<String, PascalType>();
		for (PluginDeclaration p : plugins) {
			add_callable_function(p);
		}
		this.type_generator = type_generator;
		main = new FunctionDeclaration(this);
		main.are_varargs = new boolean[0];
		main.argument_names = new String[0];
		main.argument_types = new PascalType[0];
		main.name = "main";
	}

	public PascalProgram(String program, List<PluginDeclaration> plugins,
			CustomTypeGenerator type_generator) {
		this(plugins, type_generator);
		Grouper grouper = new Grouper(program);
		new Thread(grouper).start();
		parse_tree(grouper.token_queue);
	}

	public void parse_tree(BaseGrouperToken tokens) {
		while (tokens.hasNext()) {
			add_next_declaration(tokens);
		}
	}

	private void add_next_declaration(BaseGrouperToken i) {
		Token next = i.peek();
		if (next instanceof ProcedureToken || next instanceof FunctionToken) {
			i.take();
			boolean is_procedure = next instanceof ProcedureToken;
			FunctionDeclaration declaration = new FunctionDeclaration(this,
					i, is_procedure);
			add_callable_function(declaration);
		} else if (next instanceof TypeToken) {
			i.take();
			add_custom_type_declaration(i);
		} else if (next instanceof BeginEndToken) {
			main.instructions = main.get_next_command(i);
			next = i.take();
			assert (next instanceof PeriodToken);
		} else if (next instanceof VarToken) {
			i.take();
			List<VariableDeclaration> global_var_decs = get_variable_declarations(i);
			for (VariableDeclaration v : global_var_decs) {
				main.local_variables.add(v);
			}
		} else if (next instanceof ProgramToken) {
			i.take();
			this.program_name = get_word_value(i);
			assert_next_semicolon(i);
		} else {

			i.take();
			System.err.println("Unexpected token outside of any construct: "
					+ next);
		}
	}

	public void add_callable_function(AbstractFunction f) {
		callable_functions.put(f.name().toLowerCase(), f);
	}

	private void add_custom_type_declaration(GrouperToken i) {
		CustomType result = new CustomType();
		result.name = get_word_value(i);
		Token next = i.take();
		assert (next instanceof OperatorToken);
		assert ((OperatorToken) next).type == OperatorTypes.EQUALS;
		next = i.take();
		assert (next instanceof RecordToken);
		result.variable_types = get_variable_declarations((RecordToken) next);
		custom_types.put(result.name, result);
		type_generator.output_class(result);
	}

	public List<VariableDeclaration> get_variable_declarations(GrouperToken i) {
		List<VariableDeclaration> result = new ArrayList<VariableDeclaration>();
		/*
		 * reusing it, so it is further out of scope than necessary
		 */
		List<String> names = new ArrayList<String>(1);
		Token next;
		do {
			do {
				names.add(get_word_value(i));
				next = i.take();
			} while (next instanceof CommaToken);
			assert (next instanceof ColonToken);
			PascalType type;
			try {
				type = get_next_pascal_type(i);
				assert_next_semicolon(i);
				for (String s : names) {
					result.add(new VariableDeclaration(s, type));
					/*
					 * TODO make sure this conforms to pascal
					 */
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			names.clear(); // reusing the linked list object
			next = i.peek();
		} while (next instanceof WordToken);
		return result;
	}

	public void assert_next_semicolon(GrouperToken i) {
		Token next = i.take();
		assert (next instanceof SemicolonToken);
	}

	public String get_word_value(Token t) {
		assert (t instanceof WordToken);
		return ((WordToken) t).name;
	}

	public String get_word_value(GrouperToken i) {
		return get_word_value(i.take());
	}

	PascalType get_basic_type(String s) throws ClassNotFoundException {
		s = s.toLowerCase().intern();
		if (s == "integer") {
			return JavaClassBasedType.Integer;
		}
		if (s == "string") {
			return JavaClassBasedType.StringBuilder;
		}
		if (s == "single" || s == "extended" || s == "double") {
			return JavaClassBasedType.Double;
		}
		if (s == "long") {
			return JavaClassBasedType.Long;
		}
		if (s == "boolean") {
			return JavaClassBasedType.Boolean;
		}
		if (s == "character") {
			return JavaClassBasedType.Character;
		}
		PascalType type = typedefs.get(s);
		if (type != null) {
			return type;
		} else {
			return JavaClassBasedType.anew(Class
					.forName("edu.js.interpreter.custom_types."
							+ Integer.toHexString(custom_types.get(s)
									.hashCode())));
		}
	}

	public PascalType get_next_pascal_type(GrouperToken i)
			throws ClassNotFoundException {
		String s = get_word_value(i.peek_no_EOF());
		if (s == "array") {
			ArrayList<Integer> lower = new ArrayList<Integer>(1);
			ArrayList<Integer> upper = new ArrayList<Integer>(1);
			while (s == "array") {
				i.take();
				Token next = i.take();
				if (next instanceof BracketedToken) {
					BracketedToken bounds = (BracketedToken) next;
					lower.add(((IntegerToken) bounds.take()).value);
					next = bounds.take();
					assert (next instanceof PeriodToken);
					next = bounds.take();
					assert (next instanceof PeriodToken);

					upper.add(((IntegerToken) bounds.take()).value);
					next = i.take();
				} else {
					lower.add(0);
					upper.add(-1); // So it is a zero length array
				}
				assert (next instanceof OfToken);
				s = get_word_value(i.peek_no_EOF());
			}
			i.take();
			PascalType element_class = get_basic_type(s);
			int[] lowerarray = new int[lower.size()];
			int[] sizes = new int[upper.size()];
			for (int j = 0; j < lower.size(); j++) {
				lowerarray[j] = lower.get(j);
				sizes[j] = upper.get(j) - lowerarray[j] + 1;
			}
			return new ArrayType(element_class, lowerarray, sizes);
		}
		i.take();
		return get_basic_type(s);
	}

	public void error() {
		System.err
				.println("problem with your code that the interpreter has not been taught to identify");
		System.exit(0);
	}

	public AbstractFunctionCall generate_function_call(String name,
			ReturnsValue[] args, FunctionDeclaration f) {
		List<AbstractFunction> possibilities = callable_functions.get(name
				.toLowerCase());
		ReturnsValue[] converted;
		for (AbstractFunction a : possibilities) {
			converted = a.format_args(args, f);
			if (converted != null) {
				return new AbstractFunctionCall(a, converted);
			}
		}
		System.err.println("Could not find function call " + name
				+ Arrays.toString(args));
		return null;
	}
}
