package com.js.interpreter.ast.codeunit;

import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ListMultimap;
import com.js.interpreter.ast.AbstractFunction;
import com.js.interpreter.ast.ExpressionContext;
import com.js.interpreter.ast.FunctionDeclaration;
import com.js.interpreter.ast.VariableDeclaration;
import com.js.interpreter.ast.instructions.Executable;
import com.js.interpreter.ast.instructions.returnsvalue.ReturnsValue;
import com.js.interpreter.classgeneration.CustomTypeGenerator;
import com.js.interpreter.exceptions.ExpectedTokenException;
import com.js.interpreter.exceptions.NonConstantExpressionException;
import com.js.interpreter.exceptions.OverridingFunctionException;
import com.js.interpreter.exceptions.ParsingException;
import com.js.interpreter.exceptions.UnrecognizedTokenException;
import com.js.interpreter.pascaltypes.CustomType;
import com.js.interpreter.pascaltypes.DeclaredType;
import com.js.interpreter.runtime.codeunit.RuntimeCodeUnit;
import com.js.interpreter.startup.ScriptSource;
import com.js.interpreter.tokenizer.Grouper;
import com.js.interpreter.tokens.OperatorToken;
import com.js.interpreter.tokens.OperatorTypes;
import com.js.interpreter.tokens.Token;
import com.js.interpreter.tokens.WordToken;
import com.js.interpreter.tokens.basic.ConstToken;
import com.js.interpreter.tokens.basic.FunctionToken;
import com.js.interpreter.tokens.basic.ProcedureToken;
import com.js.interpreter.tokens.basic.ProgramToken;
import com.js.interpreter.tokens.basic.TypeToken;
import com.js.interpreter.tokens.basic.VarToken;
import com.js.interpreter.tokens.grouping.BaseGrouperToken;
import com.js.interpreter.tokens.grouping.BeginEndToken;
import com.js.interpreter.tokens.grouping.GrouperToken;
import com.js.interpreter.tokens.grouping.RecordToken;

public abstract class CodeUnit implements ExpressionContext {

	Map<String, CustomType> custom_types;

	Map<String, DeclaredType> typedefs;

	String program_name;

	public Map<String, Object> constants;

	public List<VariableDeclaration> UnitVarDefs = new ArrayList<VariableDeclaration>();
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
			handleGloablVarDeclaration(i.get_variable_declarations(this));
		} else if (next instanceof ProgramToken) {
			i.take();
			this.program_name = i.next_word_value();
			i.assert_next_semicolon();
		} else if (next instanceof ConstToken) {
			i.take();
			addConstDeclarations(i);
		} else if (next instanceof TypeToken) {
			i.take();
			while (i.peek() instanceof WordToken) {
				String name = i.next_word_value();
				next = i.take();
				if (!(next instanceof OperatorToken && ((OperatorToken) next).type == OperatorTypes.EQUALS)) {
					throw new ExpectedTokenException("=", next);
				}
				typedefs.put(name, i.get_next_pascal_type(this));
				i.assert_next_semicolon();
			}
		} else {
			handleUnrecognizedToken(i.take(), i);
		}
	}

	void add_callable_function(AbstractFunction f) {
		callable_functions.put(f.name().toLowerCase(), f);
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
			ReturnsValue value = i.getNextExpression(this);
			Object comptimeval = value.compileTimeValue(this);
			if (comptimeval == null) {
				throw new NonConstantExpressionException(value);
			}
			this.constants.put(constname.name, comptimeval);
			i.assert_next_semicolon();
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
		result.name = i.next_word_value();
		Token next = i.take();
		if (!((next instanceof OperatorToken) && ((OperatorToken) next).type == OperatorTypes.EQUALS)) {
			throw new ExpectedTokenException("=", next);
		}
		next = i.take();
		if (!(next instanceof RecordToken)) {
			throw new ExpectedTokenException("record", next);
		}
		result.variable_types = ((RecordToken) next)
				.get_variable_declarations(this);
		custom_types.put(result.name, result);
		type_generator.output_class(result);
	}

	@Override
	public DeclaredType getVariableType(String ident) {
		for (VariableDeclaration v : UnitVarDefs) {
			if (v.name.equals(ident)) {
				return v.type;
			}
		}
		return null;
	}

	@Override
	public List<AbstractFunction> getCallableFunctions(String name) {
		return callable_functions.get(name);
	}

	@Override
	public Object getConstant(String ident) {
		return constants.get(ident);
	}

	@Override
	public DeclaredType getTypedefType(String ident) {
		return typedefs.get(ident);
	}

	@Override
	public CodeUnit root() {
		return this;
	}
}
