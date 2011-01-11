package com.js.interpreter.ast.codeunit;

import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ListMultimap;
import com.js.interpreter.ast.AbstractFunction;
import com.js.interpreter.ast.FunctionDeclaration;
import com.js.interpreter.ast.VariableDeclaration;
import com.js.interpreter.ast.instructions.Executable;
import com.js.interpreter.ast.instructions.returnsvalue.FunctionCall;
import com.js.interpreter.ast.instructions.returnsvalue.ReturnsValue;
import com.js.interpreter.classgeneration.CustomTypeGenerator;
import com.js.interpreter.exceptions.AmbiguousFunctionCallException;
import com.js.interpreter.exceptions.BadFunctionCallException;
import com.js.interpreter.exceptions.ExpectedAnotherTokenException;
import com.js.interpreter.exceptions.ExpectedTokenException;
import com.js.interpreter.exceptions.GroupingException;
import com.js.interpreter.exceptions.OverridingFunctionException;
import com.js.interpreter.exceptions.ParsingException;
import com.js.interpreter.exceptions.UnrecognizedTokenException;
import com.js.interpreter.exceptions.UnrecognizedTypeException;
import com.js.interpreter.pascaltypes.ArrayType;
import com.js.interpreter.pascaltypes.CustomType;
import com.js.interpreter.pascaltypes.DeclaredType;
import com.js.interpreter.pascaltypes.JavaClassBasedType;
import com.js.interpreter.pascaltypes.SubrangeType;
import com.js.interpreter.runtime.codeunit.RuntimeCodeUnit;
import com.js.interpreter.startup.ScriptSource;
import com.js.interpreter.tokenizer.Grouper;
import com.js.interpreter.tokens.OperatorToken;
import com.js.interpreter.tokens.OperatorTypes;
import com.js.interpreter.tokens.Token;
import com.js.interpreter.tokens.WordToken;
import com.js.interpreter.tokens.basic.ArrayToken;
import com.js.interpreter.tokens.basic.ColonToken;
import com.js.interpreter.tokens.basic.CommaToken;
import com.js.interpreter.tokens.basic.ConstToken;
import com.js.interpreter.tokens.basic.FunctionToken;
import com.js.interpreter.tokens.basic.OfToken;
import com.js.interpreter.tokens.basic.PeriodToken;
import com.js.interpreter.tokens.basic.ProcedureToken;
import com.js.interpreter.tokens.basic.ProgramToken;
import com.js.interpreter.tokens.basic.SemicolonToken;
import com.js.interpreter.tokens.basic.VarToken;
import com.js.interpreter.tokens.grouping.BaseGrouperToken;
import com.js.interpreter.tokens.grouping.BeginEndToken;
import com.js.interpreter.tokens.grouping.BracketedToken;
import com.js.interpreter.tokens.grouping.GrouperToken;
import com.js.interpreter.tokens.grouping.RecordToken;
import com.js.interpreter.tokens.grouping.TypeToken;
import com.js.interpreter.tokens.value.IntegerToken;
import com.js.interpreter.tokens.value.ValueToken;

public abstract class CodeUnit {

	Map<String, CustomType> custom_types;

	Map<String, DeclaredType> typedefs;

	String program_name;

	public Map<String, Object> constants;

	public List<VariableDeclaration> UnitVarDefs=new ArrayList<VariableDeclaration>();
	/*
	 * both plugins and functions
	 */
	private ListMultimap<String, AbstractFunction> callable_functions;

	private CustomTypeGenerator type_generator;

	public CodeUnit(ListMultimap<String, AbstractFunction> functionTable,
			CustomTypeGenerator type_generator) {
		this.type_generator = type_generator;
		constants = new HashMap<String, Object>();
		callable_functions = functionTable;
		custom_types = new HashMap<String, CustomType>();
		typedefs = new HashMap<String, DeclaredType>();
		prepareForParsing();
	}

	public CodeUnit(Reader program,
			ListMultimap<String, AbstractFunction> functionTable,
			String sourcename, List<ScriptSource> includeDirectories,
			CustomTypeGenerator type_generator) throws ParsingException {
		this(functionTable, type_generator);
		Grouper grouper = new Grouper(program, sourcename, includeDirectories);
		new Thread(grouper).start();
		parse_tree(grouper.token_queue);
	}

	void parse_tree(BaseGrouperToken tokens) throws ParsingException {
		while (tokens.hasNext()) {
			add_next_declaration(tokens);
		}
	}

	protected void handleBeginEnd(BaseGrouperToken i)
			throws ExpectedTokenException, ParsingException {
		i.take();
	}

	private FunctionDeclaration get_function_declaration(FunctionDeclaration f)
			throws OverridingFunctionException {
		for (AbstractFunction g : callable_functions.get(f.name)) {
			if (f.headerMatches(g)) {
				if (!(g instanceof FunctionDeclaration)) {
					throw new OverridingFunctionException(g, f);
				}
				return (FunctionDeclaration) g;
			}
		}
		callable_functions.put(f.name, f);
		return f;
	}

	private void add_next_declaration(BaseGrouperToken i)
			throws ParsingException {
		Token next = i.peek();
		if (next instanceof ProcedureToken || next instanceof FunctionToken) {
			i.take();
			boolean is_procedure = next instanceof ProcedureToken;
			FunctionDeclaration declaration = new FunctionDeclaration(this, i,
					is_procedure);
			declaration = get_function_declaration(declaration);
			declaration.parse_function_body(i);
		} else if (next instanceof TypeToken) {
			i.take();
			add_custom_type_declaration(i);
		} else if (next instanceof BeginEndToken) {
			handleBeginEnd(i);
		} else if (next instanceof VarToken) {
			i.take();
			handleGloablVarDeclaration(get_variable_declarations(i));
		} else if (next instanceof ProgramToken) {
			i.take();
			this.program_name = get_word_value(i);
			assert_next_semicolon(i);
		} else if (next instanceof ConstToken) {
			i.take();
			addConstDeclarations(i);
		} else if (next instanceof TypeToken) {
			i.take();
			while (i.peek() instanceof WordToken) {
				String name = get_word_value(i);
				next = i.take();
				if (!(next instanceof OperatorToken && ((OperatorToken) next).type == OperatorTypes.EQUALS)) {
					throw new ExpectedTokenException("=", next);
				}
				typedefs.put(name, get_next_pascal_type(i));
				this.assert_next_semicolon(i);
			}
		} else {
			handleUnrecognizedToken(i.take(), i);
		}
	}

	void add_callable_function(AbstractFunction f) {
		callable_functions.put(f.name().toLowerCase(), f);
	}

	public List<VariableDeclaration> get_variable_declarations(GrouperToken i)
			throws ParsingException {
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
			if (!(next instanceof ColonToken)) {
				throw new ExpectedTokenException(":", next);
			}
			DeclaredType type;
			type = get_next_pascal_type(i);
			assert_next_semicolon(i);
			for (String s : names) {
				result.add(new VariableDeclaration(s, type));
				/*
				 * TODO make sure this conforms to pascal
				 */
			}
			names.clear(); // reusing the list object
			next = i.peek();
		} while (next instanceof WordToken);
		return result;
	}

	public void assert_next_semicolon(GrouperToken i) throws ParsingException {
		Token next = i.peek();
		if (!(next instanceof SemicolonToken)) {
			throw new ExpectedTokenException(";", next);
		}
		i.take();
	}

	public String get_word_value(Token t) {
		assert (t instanceof WordToken);
		return ((WordToken) t).name;
	}

	public String get_word_value(GrouperToken i)
			throws ExpectedAnotherTokenException, GroupingException {
		return get_word_value(i.take());
	}

	DeclaredType get_basic_type(WordToken s2) throws UnrecognizedTypeException {
		String s = s2.name.toLowerCase().intern();
		if (s == "integer") {
			return JavaClassBasedType.Integer;
		}
		if (s == "string") {
			return JavaClassBasedType.StringBuilder;
		}
		if (s == "single" || s == "extended" || s == "double" || s == "real") {
			return JavaClassBasedType.Double;
		}
		if (s == "long") {
			return JavaClassBasedType.Long;
		}
		if (s == "boolean") {
			return JavaClassBasedType.Boolean;
		}
		if (s == "character" || s == "char") {
			return JavaClassBasedType.Character;
		}
		DeclaredType type = typedefs.get(s);
		if (type != null) {
			return type;
		} else {
			DeclaredType result = custom_types.get(s);
			if (result == null) {
				throw new UnrecognizedTypeException(s2.lineInfo, s);
			}
			return result;
		}
	}

	SubrangeType parseSubrangeType(GrouperToken i)
			throws ExpectedTokenException, ExpectedAnotherTokenException,
			GroupingException {
		int lower = ((IntegerToken) i.take()).value;
		Token t = i.take();
		if (!(t instanceof PeriodToken)) {
			throw new ExpectedTokenException("..", t);
		}
		t = i.take();
		if (t instanceof PeriodToken) {
			t = i.take();
		}
		int upper = ((IntegerToken) t).value;
		return new SubrangeType(lower, upper - lower + 1);
	}

	DeclaredType getArrayType(BracketedToken bounds, GrouperToken i)
			throws ParsingException {
		SubrangeType bound = parseSubrangeType(bounds);
		DeclaredType elementType;
		if (bounds.hasNext()) {
			elementType = getArrayType(bounds, i);
		} else {
			Token next = i.take();
			if (!(next instanceof OfToken)) {
				throw new ExpectedTokenException("of", next);
			}
			elementType = get_next_pascal_type(i);
		}
		return new ArrayType<DeclaredType>(elementType, bound);
	}

	DeclaredType getArrayType(GrouperToken i) throws ParsingException {
		Token next = i.peek_no_EOF();
		if (next instanceof BracketedToken) {
			BracketedToken bracket = (BracketedToken) i.take();
			return getArrayType(bracket, i);
		} else if (next instanceof OfToken) {
			i.take();
			DeclaredType elementType;
			next = i.take();
			if (next instanceof ArrayToken) {
				elementType = getArrayType(i);
			} else if (next instanceof WordToken) {
				elementType = get_basic_type((WordToken) next);
			} else {
				throw new ExpectedTokenException("[Type Identifier]", next);
			}
			return new ArrayType<DeclaredType>(elementType, new SubrangeType());
		} else {
			throw new ExpectedTokenException("of", next);
		}
	}

	public DeclaredType get_next_pascal_type(GrouperToken i)
			throws ParsingException {
		Token next = i.peek_no_EOF();
		if (next instanceof ArrayToken) {
			i.take();
			return getArrayType(i);
		}
		i.take();
		if (!(next instanceof WordToken)) {
			throw new ExpectedTokenException("[Type Identifier]", next);
		}
		return get_basic_type((WordToken) next);
	}

	public FunctionCall generate_function_call(WordToken name,
			List<ReturnsValue> arguments, FunctionDeclaration f)
			throws ParsingException {
		List<AbstractFunction> possibilities = callable_functions.get(name.name
				.toLowerCase());
		boolean matching = false;
		
		FunctionCall result = null;
		AbstractFunction chosen = null;
		for (AbstractFunction a : possibilities) {
			ReturnsValue[] converted = a.format_args(arguments, f);
			if (converted != null) {
				if (result != null) {
					throw new AmbiguousFunctionCallException(name.lineInfo,
							chosen, a);
				} else {
					chosen = a;
					result = new FunctionCall(a, converted, name.lineInfo);
				}
			}
			if (a.argumentTypes().length == arguments.size()) {
				matching = true;
			}
		}
		if (result != null) {
			return result;
		} else {
			throw new BadFunctionCallException(name.lineInfo, name.name,
					!possibilities.isEmpty(), matching);
		}
	}

	protected void prepareForParsing() {
		return;
	}

	public Executable handleUnrecognizedToken(Token next, GrouperToken container)
			throws ParsingException {
		throw new UnrecognizedTokenException(next);
	}

	protected void addConstDeclarations(BaseGrouperToken i)
			throws ParsingException {
		while (i.peek() instanceof WordToken) {
			WordToken constname = (WordToken) i.take();
			Token equals = i.take();
			if (!(equals instanceof OperatorToken)
					|| ((OperatorToken) equals).type != OperatorTypes.EQUALS) {
				throw new ExpectedTokenException("=", constname);
			}
			Token value = i.take();
			if (!(value instanceof ValueToken)) {/*
												 * TODO implement compile time
												 * parsing for more complex
												 * expressions.
												 */
				throw new ExpectedTokenException("[explicit value]", value);
			}
			ValueToken val = (ValueToken) value;
			this.constants.put(constname.name, val.getValue());
			assert_next_semicolon(i);
		}
	}

	public boolean functionExists(String name) {
		return callable_functions.containsKey(name);
	}

	public abstract RuntimeCodeUnit<? extends CodeUnit> run();

	protected void handleGloablVarDeclaration(
			List<VariableDeclaration> declarations) {
		UnitVarDefs.addAll(declarations);
	}

	public DeclaredType getGlobalVarType(String name) {

		for (VariableDeclaration v : UnitVarDefs) {
			if (v.name.equals(name)) {
				return v.type;
			}
		}
		return null;
	}

	private void add_custom_type_declaration(GrouperToken i)
			throws ParsingException {
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

}
