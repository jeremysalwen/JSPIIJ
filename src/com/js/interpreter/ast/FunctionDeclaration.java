package com.js.interpreter.ast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.js.interpreter.ast.codeunit.CodeUnit;
import com.js.interpreter.ast.codeunit.Library;
import com.js.interpreter.ast.instructions.Executable;
import com.js.interpreter.ast.instructions.returnsvalue.ReturnsValue;
import com.js.interpreter.ast.instructions.returnsvalue.VariableAccess;
import com.js.interpreter.exceptions.ExpectedTokenException;
import com.js.interpreter.exceptions.OverridingFunctionException;
import com.js.interpreter.exceptions.ParsingException;
import com.js.interpreter.exceptions.SameNameException;
import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.pascaltypes.ArgumentType;
import com.js.interpreter.pascaltypes.DeclaredType;
import com.js.interpreter.pascaltypes.RuntimeType;
import com.js.interpreter.runtime.FunctionOnStack;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;
import com.js.interpreter.tokens.Token;
import com.js.interpreter.tokens.WordToken;
import com.js.interpreter.tokens.basic.ColonToken;
import com.js.interpreter.tokens.basic.CommaToken;
import com.js.interpreter.tokens.basic.ForwardToken;
import com.js.interpreter.tokens.basic.SemicolonToken;
import com.js.interpreter.tokens.basic.VarToken;
import com.js.interpreter.tokens.grouping.GrouperToken;
import com.js.interpreter.tokens.grouping.ParenthesizedToken;

public class FunctionDeclaration extends AbstractFunction implements
		ExpressionContext {

	CodeUnit root;
	ExpressionContext parentContext;
	public String name;

	public List<VariableDeclaration> local_variables = new ArrayList<VariableDeclaration>();

	public Executable instructions;

	public VariableDeclaration result_definition;

	public LineInfo line;

	/* These go together ----> */
	public String[] argument_names;

	public RuntimeType[] argument_types;

	/* <----- */

	public FunctionDeclaration(ExpressionContext parent, GrouperToken i,
			boolean is_procedure) throws ParsingException {
		this.parentContext = parent;
		this.root = parent.root();
		this.line = i.peek().lineInfo;
		name = i.next_word_value();

		get_arguments_for_declaration(i, is_procedure);
		Token next = i.peek();
		if (!(is_procedure ^ (next instanceof ColonToken))) {
			throw new ParsingException(next.lineInfo,
					"Functions must have a return type, and procedures cannot have one");
		}
		if (!is_procedure && next instanceof ColonToken) {
			i.take();
			result_definition = new VariableDeclaration("result",
					i.get_next_pascal_type(this), line);
		}
		i.assert_next_semicolon();
		next = i.peek();
		if (next instanceof VarToken) {
			i.take();
			local_variables.addAll(i.get_variable_declarations(this));
		} else {
			local_variables = new ArrayList<VariableDeclaration>();
		}
		instructions = null;
		NamedEntity n = parent.getConstantDefinition(name);
		if (n != null) {
			throw new SameNameException(n, this);
		}
		n = parent.getVariableDefinition(name);
		if (n != null) {
			throw new SameNameException(n, this);
		}
	}

	public void parse_function_body(GrouperToken i) throws ParsingException {
		Token next = i.peek_no_EOF();

		if (next instanceof ForwardToken) {
			i.take();
		} else {
			if (instructions != null) {
				throw new OverridingFunctionException(this, i.lineInfo);
			}
			instructions = i.get_next_command(this);
		}
		i.assert_next_semicolon();
	}

	public void add_local_variable(VariableDeclaration v) {
		local_variables.add(v);
	}

	public FunctionDeclaration(ExpressionContext p) {
		this.parentContext = p;
		this.root = p.root();
		this.argument_names = new String[0];
		this.argument_types = new RuntimeType[0];
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public Object call(VariableContext parentcontext,
			RuntimeExecutable<?> main, Object[] arguments)
			throws RuntimePascalException {
		if (this.root instanceof Library) {
			parentcontext = main.getLibrary((Library) this.root);
		}
		return new FunctionOnStack(parentcontext, main, this, arguments)
				.execute();
	}

	VariableDeclaration getLocalVariableDefinition(String name) {
		if (name.equalsIgnoreCase("result")) {
			return this.result_definition;
		}
		int index = StaticMethods.indexOf(argument_names, name);
		if (index != -1) {
			return new VariableDeclaration(name,
					argument_types[index].declType, line);
		} else {
			for (VariableDeclaration v : local_variables) {
				if (v.name.equals(name)) {
					return v;
				}
			}
		}
		return null;
	}

	@Override
	public VariableDeclaration getVariableDefinition(String name) {
		VariableDeclaration v = getLocalVariableDefinition(name);
		if (v != null) {
			return v;
		}
		return parentContext.getVariableDefinition(name);
	}

	@Override
	public String toString() {
		return super.toString();
	}

	private void get_arguments_for_declaration(GrouperToken i,
			boolean is_procedure) throws ParsingException { // need
		List<String> names_list = new ArrayList<String>();
		List<RuntimeType> types_list = new ArrayList<RuntimeType>();
		Token next = i.peek();
		if (next instanceof ParenthesizedToken) {
			ParenthesizedToken arguments_token = (ParenthesizedToken) i.take();
			while (arguments_token.hasNext()) {
				int j = 0; // counts number added of this type
				next = arguments_token.take();
				boolean is_varargs = false;
				if (next instanceof VarToken) {
					is_varargs = true;
					next = arguments_token.take();
				}
				while (true) {
					names_list.add(((WordToken) next).name);
					j++;
					next = arguments_token.take();
					if (next instanceof CommaToken) {
						next = arguments_token.take();
					} else {
						break;
					}
				}

				if (!(next instanceof ColonToken)) {
					throw new ExpectedTokenException(":", next);
				}
				DeclaredType type;
				type = arguments_token.get_next_pascal_type(this);

				while (j > 0) {
					types_list.add(new RuntimeType(type, is_varargs));
					j--;
				}
				if (arguments_token.hasNext()) {
					next = arguments_token.take();
					if (!(next instanceof SemicolonToken)) {
						throw new ExpectedTokenException(";", next);
					}
				}
			}
		}
		argument_types = types_list.toArray(new RuntimeType[types_list.size()]);
		argument_names = names_list.toArray(new String[names_list.size()]);

	}

	@Override
	public ArgumentType[] argumentTypes() {
		return argument_types;
	}

	@Override
	public DeclaredType return_type() {
		return result_definition == null ? null : result_definition.type;
	}

	public boolean headerMatches(AbstractFunction other)
			throws ParsingException {
		if (name.equals(other.name())
				&& Arrays.equals(argument_types, other.argumentTypes())) {
			if (result_definition == null && other.return_type() == null) {
				return true;
			}
			if (result_definition == null || other.return_type() == null
					|| !result_definition.equals(other.return_type())) {
				System.err
						.println("Warning: Overriding previously declared return type for function "
								+ name);
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean functionExists(String name) {
		return parentContext.functionExists(name);
	}

	@Override
	public List<AbstractFunction> getCallableFunctions(String name) {
		return parentContext.getCallableFunctions(name);
	}

	@Override
	public ConstantDefinition getConstantDefinition(String ident) {
		return parentContext.getConstantDefinition(ident);
	}

	@Override
	public DeclaredType getTypedefType(String ident) {
		return parentContext.getTypedefType(ident);
	}

	@Override
	public CodeUnit root() {
		return root;
	}

	@Override
	public String getEntityType() {
		return "function";
	}

	@Override
	public LineInfo getLineNumber() {
		return line;
	}

	@Override
	public void verifyNonConflictingSymbol(NamedEntity n)
			throws SameNameException {
		VariableDeclaration v = getLocalVariableDefinition(n.name());
		if (v != null) {
			throw new SameNameException(v, n);
		}
	}

	@Override
	public ReturnsValue getIdentifierValue(WordToken name)
			throws ParsingException {
		if (getLocalVariableDefinition(name.name) != null) {
			return new VariableAccess(name);
		}
		return parentContext.getIdentifierValue(name);
	}
}
