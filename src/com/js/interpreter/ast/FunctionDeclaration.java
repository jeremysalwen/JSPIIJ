package com.js.interpreter.ast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.js.interpreter.ast.codeunit.CodeUnit;
import com.js.interpreter.ast.codeunit.Library;
import com.js.interpreter.ast.instructions.Executable;
import com.js.interpreter.ast.instructions.InstructionGrouper;
import com.js.interpreter.ast.instructions.NopInstruction;
import com.js.interpreter.ast.instructions.VariableSet;
import com.js.interpreter.ast.instructions.case_statement.CaseInstruction;
import com.js.interpreter.ast.instructions.conditional.DowntoForStatement;
import com.js.interpreter.ast.instructions.conditional.ForStatement;
import com.js.interpreter.ast.instructions.conditional.IfStatement;
import com.js.interpreter.ast.instructions.conditional.RepeatInstruction;
import com.js.interpreter.ast.instructions.conditional.WhileStatement;
import com.js.interpreter.ast.instructions.returnsvalue.FunctionCall;
import com.js.interpreter.ast.instructions.returnsvalue.ReturnsValue;
import com.js.interpreter.exceptions.ExpectedTokenException;
import com.js.interpreter.exceptions.OverridingFunctionException;
import com.js.interpreter.exceptions.ParsingException;
import com.js.interpreter.exceptions.SameNameException;
import com.js.interpreter.exceptions.UnconvertableTypeException;
import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.pascaltypes.ArgumentType;
import com.js.interpreter.pascaltypes.DeclaredType;
import com.js.interpreter.pascaltypes.RuntimeType;
import com.js.interpreter.runtime.FunctionOnStack;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;
import com.js.interpreter.runtime.variables.VariableIdentifier;
import com.js.interpreter.tokens.EOF_Token;
import com.js.interpreter.tokens.Token;
import com.js.interpreter.tokens.WordToken;
import com.js.interpreter.tokens.basic.AssignmentToken;
import com.js.interpreter.tokens.basic.ColonToken;
import com.js.interpreter.tokens.basic.CommaToken;
import com.js.interpreter.tokens.basic.DoToken;
import com.js.interpreter.tokens.basic.DowntoToken;
import com.js.interpreter.tokens.basic.ElseToken;
import com.js.interpreter.tokens.basic.ForToken;
import com.js.interpreter.tokens.basic.ForwardToken;
import com.js.interpreter.tokens.basic.IfToken;
import com.js.interpreter.tokens.basic.OfToken;
import com.js.interpreter.tokens.basic.RepeatToken;
import com.js.interpreter.tokens.basic.SemicolonToken;
import com.js.interpreter.tokens.basic.ThenToken;
import com.js.interpreter.tokens.basic.ToToken;
import com.js.interpreter.tokens.basic.UntilToken;
import com.js.interpreter.tokens.basic.VarToken;
import com.js.interpreter.tokens.basic.WhileToken;
import com.js.interpreter.tokens.grouping.BeginEndToken;
import com.js.interpreter.tokens.grouping.CaseToken;
import com.js.interpreter.tokens.grouping.GrouperToken;
import com.js.interpreter.tokens.grouping.ParenthesizedToken;

public class FunctionDeclaration extends AbstractFunction implements
		ExpressionContext {

	CodeUnit root;
	ExpressionContext parentContext;
	public String name;

	public List<VariableDeclaration> local_variables;

	public Executable instructions;

	public VariableDeclaration return_type;

	public LineInfo line;

	/* These go together ----> */
	public String[] argument_names;

	public RuntimeType[] argument_types;

	public boolean[] are_varargs;

	/* <----- */

	public FunctionDeclaration(ExpressionContext parent, GrouperToken i,
			boolean is_procedure) throws ParsingException {
		this.parentContext = parent;
		this.root = parent.root();
		this.line = i.peek().lineInfo;
		instructions = new InstructionGrouper(i.peek_no_EOF().lineInfo);
		name = i.next_word_value();
		if (parent.getVariableDefinition(name) != null) {
			throw new SameNameException(line, getVariableDefinition(name),
					this, name);
		} else if (parent.getConstantDefinition(name) != null) {
			throw new SameNameException(line, getConstantDefinition(name),
					this, name);
		}
		get_arguments_for_declaration(i, is_procedure);
		Token next = i.peek();
		if (!(is_procedure ^ (next instanceof ColonToken))) {
			throw new ParsingException(next.lineInfo,
					"Functions must have a return type, and procedures cannot have one");
		}
		if (!is_procedure && next instanceof ColonToken) {
			i.take();
			return_type = new VariableDeclaration("resiult", i
					.get_next_pascal_type(this), line);
		}
		i.assert_next_semicolon();
		next = i.peek();
		if (next instanceof VarToken) {
			i.take();
			local_variables = i.get_variable_declarations(this);
		} else {
			local_variables = new ArrayList<VariableDeclaration>();
		}
		instructions = null;
	}

	public void parse_function_body(GrouperToken i) throws ParsingException {
		Token next = i.peek_no_EOF();

		if (next instanceof ForwardToken) {
			i.take();
		} else {
			if (instructions != null) {
				throw new OverridingFunctionException(this, i.lineInfo);
			}
			instructions = get_next_command(i);
		}
		i.assert_next_semicolon();
	}

	public void add_local_variable(VariableDeclaration v) {
		local_variables.add(v);
	}

	public FunctionDeclaration(ExpressionContext p) {
		this.parentContext = p;
		this.root = p.root();
		this.local_variables = new ArrayList<VariableDeclaration>();
		this.are_varargs = new boolean[0];
		this.argument_names = new String[0];
		this.argument_types = new RuntimeType[0];
	}

	public FunctionDeclaration(ExpressionContext parent,
			List<VariableDeclaration> local_variables,
			InstructionGrouper instructions) {
		this.parentContext = parent;
		this.root = parent.root();
		this.local_variables = local_variables;
		this.instructions = instructions;
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

	public VariableDeclaration getVariableDefinition(String name) {
		if (name.equalsIgnoreCase("result")) {
			return this.return_type;
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
		return parentContext.getVariableDefinition(name);
	}

	@Override
	public boolean isByReference(int i) {
		return are_varargs[i];
	}

	@Override
	public String toString() {
		return super.toString();
	}

	private void get_arguments_for_declaration(GrouperToken i,
			boolean is_procedure) throws ParsingException { // need
		List<Boolean> are_varargs_list = new ArrayList<Boolean>();
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
					are_varargs_list.add(is_varargs);
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
		are_varargs = new boolean[are_varargs_list.size()];
		for (int k = 0; k < are_varargs.length; k++) {
			are_varargs[k] = are_varargs_list.get(k);
		}
	}

	public Executable get_next_command(GrouperToken token_iterator)
			throws ParsingException {
		Token next = token_iterator.take();
		LineInfo initialline = next.lineInfo;
		if (next instanceof IfToken) {
			ReturnsValue condition = token_iterator.getNextExpression(this);
			next = token_iterator.take();
			assert (next instanceof ThenToken);
			Executable command = get_next_command(token_iterator);
			Executable else_command = null;
			next = token_iterator.peek();
			if (next instanceof ElseToken) {
				token_iterator.take();
				else_command = get_next_command(token_iterator);
			}
			return new IfStatement(condition, command, else_command,
					initialline);
		} else if (next instanceof WhileToken) {
			ReturnsValue condition = token_iterator.getNextExpression(this);
			next = token_iterator.take();
			assert (next instanceof DoToken);
			Executable command = get_next_command(token_iterator);
			return new WhileStatement(condition, command, initialline);
		} else if (next instanceof BeginEndToken) {
			InstructionGrouper begin_end_preprocessed = new InstructionGrouper(
					initialline);
			BeginEndToken cast_token = (BeginEndToken) next;
			if (cast_token.hasNext()) {

			}
			while (cast_token.hasNext()) {
				begin_end_preprocessed
						.add_command(get_next_command(cast_token));
				if (cast_token.hasNext()) {
					cast_token.assert_next_semicolon();
				}
			}
			return begin_end_preprocessed;
		} else if (next instanceof ForToken) {
			VariableIdentifier tmp_var = token_iterator
					.get_next_var_identifier(this);
			next = token_iterator.take();
			assert (next instanceof AssignmentToken);
			ReturnsValue first_value = token_iterator.getNextExpression(this);
			next = token_iterator.take();
			boolean downto = false;
			if (next instanceof DowntoToken) {
				downto = true;
			} else if (!(next instanceof ToToken)) {
				throw new ExpectedTokenException("[To] or [Downto]", next);
			}
			ReturnsValue last_value = token_iterator.getNextExpression(this);
			next = token_iterator.take();
			assert (next instanceof DoToken);
			Executable result;
			if (downto) { // TODO probably should merge these two types
				result = new DowntoForStatement(tmp_var, first_value,
						last_value, get_next_command(token_iterator),
						initialline);
			} else {
				result = new ForStatement(tmp_var, first_value, last_value,
						get_next_command(token_iterator), initialline);
			}
			return result;
		} else if (next instanceof RepeatToken) {
			InstructionGrouper command = new InstructionGrouper(initialline);

			while (!(token_iterator.peek_no_EOF() instanceof UntilToken)) {
				command.add_command(get_next_command(token_iterator));
				if (!(token_iterator.peek_no_EOF() instanceof UntilToken)) {
					token_iterator.assert_next_semicolon();
				}
			}
			next = token_iterator.take();
			if (!(next instanceof UntilToken)) {
				throw new ExpectedTokenException("until", next);
			}
			ReturnsValue condition = token_iterator.getNextExpression(this);
			return new RepeatInstruction(command, condition, initialline);
		} else if (next instanceof WordToken) {

			WordToken nametoken = (WordToken) next;
			next = token_iterator.peek();
			if (next instanceof ParenthesizedToken) {
				next = token_iterator.take();
				List<ReturnsValue> arguments = ((ParenthesizedToken) next)
						.get_arguments_for_call(this);
				return FunctionCall.generate_function_call(nametoken,
						arguments, this);
			} else if (next instanceof SemicolonToken
					|| next instanceof EOF_Token) {
				List<ReturnsValue> arguments = new ArrayList<ReturnsValue>();
				return FunctionCall.generate_function_call(nametoken,
						arguments, this);
			} else {
				// at this point assuming it is a variable identifier.
				VariableIdentifier identifier = token_iterator
						.get_next_var_identifier(this, nametoken);
				next = token_iterator.take();
				if (!(next instanceof AssignmentToken)) {
					throw new ExpectedTokenException(":=", next);
				}
				ReturnsValue value_to_assign = token_iterator
						.getNextExpression(this);
				DeclaredType output_type = identifier.get_type(this).declType;
				DeclaredType input_type = value_to_assign.get_type(this).declType;
				/*
				 * Does not have to be writable to assign value to variable.
				 */
				value_to_assign = output_type.convert(value_to_assign, this);
				if (value_to_assign == null) {
					throw new UnconvertableTypeException(next.lineInfo,
							input_type, output_type);
				}
				return new VariableSet(identifier, value_to_assign, initialline);
			}
		} else if (next instanceof CaseToken) {
			CaseToken grouper = (CaseToken) next;
			ReturnsValue switchvalue = grouper.getNextExpression(this);
			next = grouper.take();
			if (!(next instanceof OfToken)) {
				throw new ExpectedTokenException("of", next);
			}
			CaseInstruction inst = new CaseInstruction(initialline);
			next = grouper.take();
			while (!(next instanceof ElseToken || next instanceof EOF_Token)) {
				ReturnsValue possibility = grouper.getNextExpression(this);

			}
		} else if (next instanceof SemicolonToken) {
			return new NopInstruction(next.lineInfo);
		}
		return root.handleUnrecognizedToken(next, token_iterator);
	}

	@Override
	public ArgumentType[] argumentTypes() {
		return argument_types;
	}

	@Override
	public DeclaredType return_type() {
		return return_type.type;
	}

	public boolean headerMatches(AbstractFunction other) {
		if (name.equals(other.name())
				&& Arrays.equals(argument_types, other.argumentTypes())) {
			if (!(other instanceof FunctionDeclaration)) {
				System.err
						.println("Warning: attempting to override plugin declaration "
								+ name + " with pascal code");
				return false;
			}
			if (!return_type.equals(other.return_type())) {
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
}
