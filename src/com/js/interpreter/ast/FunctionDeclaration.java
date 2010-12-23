package com.js.interpreter.ast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.js.interpreter.ast.codeunit.CodeUnit;
import com.js.interpreter.ast.codeunit.Library;
import com.js.interpreter.ast.instructions.Executable;
import com.js.interpreter.ast.instructions.InstructionGrouper;
import com.js.interpreter.ast.instructions.VariableSet;
import com.js.interpreter.ast.instructions.case_statement.CaseInstruction;
import com.js.interpreter.ast.instructions.conditional.DowntoForStatement;
import com.js.interpreter.ast.instructions.conditional.ForStatement;
import com.js.interpreter.ast.instructions.conditional.IfStatement;
import com.js.interpreter.ast.instructions.conditional.RepeatInstruction;
import com.js.interpreter.ast.instructions.conditional.WhileStatement;
import com.js.interpreter.ast.instructions.returnsvalue.BinaryOperatorEvaluation;
import com.js.interpreter.ast.instructions.returnsvalue.ConstantAccess;
import com.js.interpreter.ast.instructions.returnsvalue.ReturnsValue;
import com.js.interpreter.ast.instructions.returnsvalue.UnaryOperatorEvaluation;
import com.js.interpreter.ast.instructions.returnsvalue.VariableAccess;
import com.js.interpreter.exceptions.BadOperationTypeException;
import com.js.interpreter.exceptions.ExpectedTokenException;
import com.js.interpreter.exceptions.NoSuchFunctionOrVariableException;
import com.js.interpreter.exceptions.ParsingException;
import com.js.interpreter.exceptions.UnconvertableTypeException;
import com.js.interpreter.exceptions.UnrecognizedTokenException;
import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.pascaltypes.ArgumentType;
import com.js.interpreter.pascaltypes.DeclaredType;
import com.js.interpreter.pascaltypes.RuntimeType;
import com.js.interpreter.runtime.FunctionOnStack;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;
import com.js.interpreter.runtime.variables.ReturnsValue_SubvarIdentifier;
import com.js.interpreter.runtime.variables.String_SubvarIdentifier;
import com.js.interpreter.runtime.variables.VariableIdentifier;
import com.js.interpreter.tokens.EOF_Token;
import com.js.interpreter.tokens.OperatorToken;
import com.js.interpreter.tokens.OperatorTypes;
import com.js.interpreter.tokens.OperatorTypes.precedence;
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
import com.js.interpreter.tokens.basic.PeriodToken;
import com.js.interpreter.tokens.basic.RepeatToken;
import com.js.interpreter.tokens.basic.SemicolonToken;
import com.js.interpreter.tokens.basic.ThenToken;
import com.js.interpreter.tokens.basic.ToToken;
import com.js.interpreter.tokens.basic.UntilToken;
import com.js.interpreter.tokens.basic.VarToken;
import com.js.interpreter.tokens.basic.WhileToken;
import com.js.interpreter.tokens.grouping.BeginEndToken;
import com.js.interpreter.tokens.grouping.BracketedToken;
import com.js.interpreter.tokens.grouping.CaseToken;
import com.js.interpreter.tokens.grouping.GrouperToken;
import com.js.interpreter.tokens.grouping.ParenthesizedToken;
import com.js.interpreter.tokens.value.ValueToken;

public class FunctionDeclaration extends AbstractFunction {
	public CodeUnit program;

	public String name;

	public List<VariableDeclaration> local_variables;

	public Executable instructions;

	public DeclaredType return_type;

	/* These go together ----> */
	public String[] argument_names;

	public RuntimeType[] argument_types;

	public boolean[] are_varargs;

	/* <----- */

	public FunctionDeclaration(CodeUnit p, GrouperToken i, boolean is_procedure)
			throws ParsingException {
		this.program = p;
		instructions = new InstructionGrouper(i.peek_no_EOF().lineInfo);
		name = program.get_word_value(i);
		get_arguments_for_declaration(i, is_procedure);
		Token next = i.peek();
		assert (is_procedure ^ (next instanceof ColonToken));
		if (!is_procedure && next instanceof ColonToken) {
			i.take();
			return_type = program.get_next_pascal_type(i);
		}
		program.assert_next_semicolon(i);
		next = i.peek();
		if (next instanceof VarToken) {
			i.take();
			local_variables = program.get_variable_declarations(i);
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
				throw new ParsingException(next.lineInfo,
						"Multiple definitions of " + toString());
			}
			instructions = get_next_command(i);
		}
		program.assert_next_semicolon(i);
	}

	public void add_local_variable(VariableDeclaration v) {
		local_variables.add(v);
	}

	public FunctionDeclaration(CodeUnit p) {
		this.program = p;
		this.local_variables = new ArrayList<VariableDeclaration>();
		this.are_varargs = new boolean[0];
		this.argument_names = new String[0];
		this.argument_types = new RuntimeType[0];
	}

	public FunctionDeclaration(CodeUnit parent,
			List<VariableDeclaration> local_variables,
			InstructionGrouper instructions) {
		this.program = parent;
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
		if (this.program instanceof Library) {
			parentcontext = main.getLibrary((Library) this.program);
		}
		return new FunctionOnStack(parentcontext, main, this, arguments)
				.execute();
	}

	public DeclaredType get_variable_type(String name) {
		if (name.equalsIgnoreCase("result")) {
			return return_type;
		}
		int index = StaticMethods.indexOf(argument_names, name);
		if (index != -1) {
			return argument_types[index].declType;
		} else {
			for (VariableDeclaration v : local_variables) {
				if (v.name.equals(name)) {
					return v.type;
				}
			}
		}
		return null;
	}

	@Override
	public boolean isByReference(int i) {
		return are_varargs[i];
	}

	@Override
	public String toString() {
		return super.toString() + ",function";
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

				assert (next instanceof ColonToken);
				DeclaredType type;
				type = program.get_next_pascal_type(arguments_token);

				while (j > 0) {
					types_list.add(new RuntimeType(type, is_varargs));
					j--;
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

	ReturnsValue getNextTerm(GrouperToken iterator) throws ParsingException {
		Token next = iterator.take();
		if (next instanceof ParenthesizedToken) {
			return get_single_value((ParenthesizedToken) next);
		} else if (next instanceof ValueToken) {
			return new ConstantAccess(((ValueToken) next).getValue(),
					next.lineInfo);
		} else if (next instanceof WordToken) {
			WordToken name = ((WordToken) next);
			next = iterator.peek();

			if (next instanceof ParenthesizedToken) {
				List<ReturnsValue> arguments = get_arguments_for_call((ParenthesizedToken) iterator
						.take());
				return program.generate_function_call(name, arguments, this);
			} else if (program.functionExists(name.name)) {
				return program.generate_function_call(name,
						new ArrayList<ReturnsValue>(0), this);
			}
			VariableAccess result = new VariableAccess(get_next_var_identifier(
					name, iterator), name.lineInfo);
			if (result.get_type(this) == null) {
				throw new NoSuchFunctionOrVariableException(name.lineInfo,
						name.name);
			}
			return result;

		} else {
			throw new UnrecognizedTokenException(next);
		}
	}

	ReturnsValue getNextExpression(GrouperToken iterator,
			OperatorTypes.precedence precedence) throws ParsingException {
		ReturnsValue nextTerm;
		Token next = iterator.peek();
		if (next instanceof OperatorToken) {
			iterator.take();
			OperatorToken nextOperator = (OperatorToken) next;
			if (!nextOperator.can_be_unary()) {
				throw new BadOperationTypeException(next.lineInfo,
						nextOperator.type);
			}
			nextTerm = new UnaryOperatorEvaluation(getNextExpression(iterator,
					nextOperator.type.getPrecedence()), nextOperator.type,
					nextOperator.lineInfo);
		} else {
			nextTerm = getNextTerm(iterator);
		}

		while ((next = iterator.peek()) instanceof OperatorToken) {
			OperatorToken nextOperator = (OperatorToken) next;
			if (nextOperator.type.getPrecedence().compareTo(precedence) >= 0) {
				break;
			}
			iterator.take();
			ReturnsValue nextvalue = getNextExpression(iterator,
					nextOperator.type.getPrecedence());
			OperatorTypes operationtype = ((OperatorToken) next).type;
			DeclaredType type1 = nextTerm.get_type(this).declType;
			DeclaredType type2 = nextvalue.get_type(this).declType;
			try {
				operationtype.verifyOperation(type1, type2);
			} catch (BadOperationTypeException e) {
				throw new BadOperationTypeException(next.lineInfo, type1,
						type2, nextTerm, nextvalue, operationtype);
			}
			nextTerm = new BinaryOperatorEvaluation(nextTerm, nextvalue,
					operationtype, nextOperator.lineInfo);

		}
		return nextTerm;
	}

	ReturnsValue getNextExpression(GrouperToken iterator)
			throws ParsingException {
		return getNextExpression(iterator, precedence.NoPrecedence);
	}

	VariableIdentifier get_next_var_identifier(GrouperToken i)
			throws ParsingException {
		Token next;
		WordToken nametoken = (WordToken) i.take();

		VariableIdentifier identifier = new VariableIdentifier(
				nametoken.lineInfo);
		identifier.add(new String_SubvarIdentifier(nametoken.name));
		while (true) {
			if (i.peek() instanceof PeriodToken) {
				i.take();
				next = i.take();
				if (next instanceof WordToken) {
					identifier.add(new String_SubvarIdentifier(
							((WordToken) next).name));
				} else {
					throw new ExpectedTokenException(next.lineInfo,
							"[Variable Identifier]");
				}
			} else if (i.peek() instanceof BracketedToken) {
				identifier.add(new ReturnsValue_SubvarIdentifier(
						getNextExpression((BracketedToken) i.take())));
			} else {
				break;
			}
		}
		return identifier;
	}

	VariableIdentifier get_next_var_identifier(WordToken initial, GrouperToken i)
			throws ParsingException {
		Token next;
		VariableIdentifier identifier = new VariableIdentifier(initial.lineInfo);
		identifier.add(new String_SubvarIdentifier(initial.name));
		while (true) {
			if (i.peek() instanceof PeriodToken) {
				i.take();
				next = i.take();
				if (next instanceof WordToken) {
					identifier.add(new String_SubvarIdentifier(
							((WordToken) next).name));
				} else {
					throw new UnrecognizedTokenException(next);
				}
			} else if (i.peek() instanceof BracketedToken) {
				identifier.add(new ReturnsValue_SubvarIdentifier(
						getNextExpression((BracketedToken) i.take())));
			} else {
				break;
			}
		}
		return identifier;
	}

	public Executable get_next_command(GrouperToken token_iterator)
			throws ParsingException {
		Token next = token_iterator.take();
		LineInfo initialline = next.lineInfo;
		if (next instanceof IfToken) {
			ReturnsValue condition = getNextExpression(token_iterator);
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
			ReturnsValue condition = getNextExpression(token_iterator);
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
					program.assert_next_semicolon(cast_token);
				}
			}
			return begin_end_preprocessed;
		} else if (next instanceof ForToken) {
			VariableIdentifier tmp_var = get_next_var_identifier(token_iterator);
			next = token_iterator.take();
			assert (next instanceof AssignmentToken);
			ReturnsValue first_value = getNextExpression(token_iterator);
			next = token_iterator.take();
			boolean downto = false;
			if (next instanceof DowntoToken) {
				downto = true;
			} else if (!(next instanceof ToToken)) {
				throw new ExpectedTokenException(next.lineInfo,
						"[To] or [Downto]");
			}
			ReturnsValue last_value = getNextExpression(token_iterator);
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
					program.assert_next_semicolon(token_iterator);
				}
			}
			next = token_iterator.take();
			if (!(next instanceof UntilToken)) {
				throw new ExpectedTokenException(next.lineInfo, "until");
			}
			ReturnsValue condition = getNextExpression(token_iterator);
			return new RepeatInstruction(command, condition, initialline);
		} else if (next instanceof WordToken) {

			WordToken nametoken = (WordToken) next;
			next = token_iterator.peek();
			if (next instanceof ParenthesizedToken) {
				token_iterator.take();
				List<ReturnsValue> arguments = get_arguments_for_call((ParenthesizedToken) next);
				return program.generate_function_call(nametoken, arguments,
						this);
			} else if (next instanceof SemicolonToken
					|| next instanceof EOF_Token) {
				List<ReturnsValue> arguments = new ArrayList<ReturnsValue>();
				return program.generate_function_call(nametoken, arguments,
						this);
			} else {
				// at this point assuming it is a variable identifier.
				VariableIdentifier identifier = get_next_var_identifier(
						nametoken, token_iterator);
				next = token_iterator.take();
				if (!(next instanceof AssignmentToken)) {
					throw new ExpectedTokenException(next.lineInfo, ":=");
				}
				ReturnsValue value_to_assign = getNextExpression(token_iterator);
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
			ReturnsValue switchvalue = getNextExpression(grouper);
			next = grouper.take();
			if (!(next instanceof OfToken)) {
				throw new ExpectedTokenException(next.lineInfo, "of");
			}
			CaseInstruction inst = new CaseInstruction(initialline);
			next = grouper.take();
			while (!(next instanceof ElseToken || next instanceof EOF_Token)) {
				ReturnsValue possibility = getNextExpression(grouper);

			}
		}
		return program.handleUnrecognizedToken(next, token_iterator);
	}

	List<ReturnsValue> get_arguments_for_call(ParenthesizedToken t)
			throws ParsingException {
		List<ReturnsValue> result = new ArrayList<ReturnsValue>();
		while (t.hasNext()) {
			result.add(getNextExpression(t));
			if (t.hasNext()) {
				Token next = t.take();
				assert (next instanceof CommaToken);
			}
		}
		return result;
	}

	ReturnsValue get_single_value(ParenthesizedToken t) throws ParsingException {
		ReturnsValue result = getNextExpression(t);
		if (t.hasNext()) {
			Token next = t.take();
			throw new UnrecognizedTokenException(next);
		}
		return result;
	}

	@Override
	public ArgumentType[] argumentTypes() {
		return argument_types;
	}

	@Override
	public DeclaredType return_type() {
		return return_type;
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
}
