package com.js.interpreter.tokens.grouping;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import com.js.interpreter.ast.ExpressionContext;
import com.js.interpreter.ast.VariableDeclaration;
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
import com.js.interpreter.ast.instructions.returnsvalue.BinaryOperatorEvaluation;
import com.js.interpreter.ast.instructions.returnsvalue.ConstantAccess;
import com.js.interpreter.ast.instructions.returnsvalue.FunctionCall;
import com.js.interpreter.ast.instructions.returnsvalue.ReturnsValue;
import com.js.interpreter.ast.instructions.returnsvalue.UnaryOperatorEvaluation;
import com.js.interpreter.ast.instructions.returnsvalue.VariableAccess;
import com.js.interpreter.exceptions.BadOperationTypeException;
import com.js.interpreter.exceptions.ExpectedAnotherTokenException;
import com.js.interpreter.exceptions.ExpectedTokenException;
import com.js.interpreter.exceptions.GroupingException;
import com.js.interpreter.exceptions.NoSuchFunctionOrVariableException;
import com.js.interpreter.exceptions.ParsingException;
import com.js.interpreter.exceptions.SameNameException;
import com.js.interpreter.exceptions.UnconvertableTypeException;
import com.js.interpreter.exceptions.UnrecognizedTokenException;
import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.pascaltypes.ArrayType;
import com.js.interpreter.pascaltypes.DeclaredType;
import com.js.interpreter.pascaltypes.SubrangeType;
import com.js.interpreter.runtime.variables.ReturnsValue_SubvarIdentifier;
import com.js.interpreter.runtime.variables.String_SubvarIdentifier;
import com.js.interpreter.runtime.variables.SubvarIdentifier;
import com.js.interpreter.runtime.variables.VariableIdentifier;
import com.js.interpreter.tokens.EOF_Token;
import com.js.interpreter.tokens.GroupingExceptionToken;
import com.js.interpreter.tokens.OperatorToken;
import com.js.interpreter.tokens.OperatorTypes;
import com.js.interpreter.tokens.Token;
import com.js.interpreter.tokens.WordToken;
import com.js.interpreter.tokens.OperatorTypes.precedence;
import com.js.interpreter.tokens.basic.ArrayToken;
import com.js.interpreter.tokens.basic.AssignmentToken;
import com.js.interpreter.tokens.basic.ColonToken;
import com.js.interpreter.tokens.basic.CommaToken;
import com.js.interpreter.tokens.basic.DoToken;
import com.js.interpreter.tokens.basic.DowntoToken;
import com.js.interpreter.tokens.basic.ElseToken;
import com.js.interpreter.tokens.basic.ForToken;
import com.js.interpreter.tokens.basic.IfToken;
import com.js.interpreter.tokens.basic.OfToken;
import com.js.interpreter.tokens.basic.PeriodToken;
import com.js.interpreter.tokens.basic.RepeatToken;
import com.js.interpreter.tokens.basic.SemicolonToken;
import com.js.interpreter.tokens.basic.ThenToken;
import com.js.interpreter.tokens.basic.ToToken;
import com.js.interpreter.tokens.basic.UntilToken;
import com.js.interpreter.tokens.basic.WhileToken;
import com.js.interpreter.tokens.value.ValueToken;

public abstract class GrouperToken extends Token {
	LinkedBlockingQueue<Token> queue;

	Token next = null;

	private Token get_next() throws GroupingException {
		if (next == null) {
			while (true) {
				try {
					next = queue.take();
				} catch (InterruptedException e) {
					continue;
				}
				break;
			}
		}
		exceptioncheck(next);
		return next;
	}

	public GrouperToken(LineInfo line) {
		super(line);
		queue = new LinkedBlockingQueue<Token>();
	}

	public boolean hasNext() throws GroupingException {
		return !(get_next() instanceof EOF_Token);
	}

	private void exceptioncheck(Token t) throws GroupingException {
		if (t instanceof GroupingExceptionToken) {
			throw ((GroupingExceptionToken) t).exception;
		}
	}

	public void put(Token t) {
		while (true) {
			try {
				queue.put(t);
			} catch (InterruptedException e) {
				continue;
			}
			break;
		}
	}

	public Token take() throws ExpectedAnotherTokenException, GroupingException {
		Token result = get_next();
		if (result instanceof EOF_Token) {
			throw new ExpectedAnotherTokenException(result.lineInfo);
		}
		while (true) {
			try {
				next = queue.take();
				exceptioncheck(next);
				return result;
			} catch (InterruptedException e) {
			}
		}
	}

	public Token peek() throws GroupingException {
		return get_next();
	}

	public Token peek_no_EOF() throws ExpectedAnotherTokenException,
			GroupingException {
		Token result = peek();
		if (result instanceof EOF_Token) {
			throw new ExpectedAnotherTokenException(result.lineInfo);
		}
		return result;
	}

	@Override
	public String toString() {
		try {
			return get_next().toString() + ',' + queue.toString();
		} catch (GroupingException e) {
			return "Exception: " + e.toString();
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -5736870403548847904L;

	public String next_word_value() throws ParsingException {
		return take().get_word_value().name;
	}

	public void assert_next_semicolon() throws ParsingException {
		Token t = take();
		if (!(t instanceof SemicolonToken)) {
			throw new ExpectedTokenException(";", t);
		}
	}

	public DeclaredType get_next_pascal_type(ExpressionContext context)
			throws ParsingException {
		Token n = peek_no_EOF();
		if (n instanceof ArrayToken) {
			take();
			return getArrayType(context);
		}
		take();
		if (!(n instanceof WordToken)) {
			throw new ExpectedTokenException("[Type Identifier]", n);
		}
		return ((WordToken) n).to_basic_type(context);
	}

	DeclaredType getArrayType(ExpressionContext context)
			throws ParsingException {
		Token n = peek_no_EOF();
		if (n instanceof BracketedToken) {
			BracketedToken bracket = (BracketedToken) take();
			return getArrayType(bracket, context);
		} else if (n instanceof OfToken) {
			take();
			DeclaredType elementType;
			n = take();
			if (n instanceof ArrayToken) {
				elementType = getArrayType(context);
			} else if (n instanceof WordToken) {
				elementType = ((WordToken) n).to_basic_type(context);
			} else {
				throw new ExpectedTokenException("[Type Identifier]", n);
			}
			return new ArrayType<DeclaredType>(elementType, new SubrangeType());
		} else {
			throw new ExpectedTokenException("of", n);
		}
	}

	DeclaredType getArrayType(BracketedToken bounds, ExpressionContext context)
			throws ParsingException {
		SubrangeType bound = new SubrangeType(bounds, context);
		DeclaredType elementType;
		if (bounds.hasNext()) {
			elementType = getArrayType(bounds, context);
		} else {
			Token next = take();
			if (!(next instanceof OfToken)) {
				throw new ExpectedTokenException("of", next);
			}
			elementType = get_next_pascal_type(context);
		}
		return new ArrayType<DeclaredType>(elementType, bound);
	}

	public ReturnsValue getNextExpression(ExpressionContext context,
			OperatorTypes.precedence precedence) throws ParsingException {
		ReturnsValue nextTerm;
		Token next = peek();
		if (next instanceof OperatorToken) {
			take();
			OperatorToken nextOperator = (OperatorToken) next;
			if (!nextOperator.can_be_unary()) {
				throw new BadOperationTypeException(next.lineInfo,
						nextOperator.type);
			}
			nextTerm = new UnaryOperatorEvaluation(getNextExpression(context,
					nextOperator.type.getPrecedence()), nextOperator.type,
					nextOperator.lineInfo);
		} else {
			nextTerm = getNextTerm(context);
		}

		while ((next = peek()) instanceof OperatorToken) {
			OperatorToken nextOperator = (OperatorToken) next;
			if (nextOperator.type.getPrecedence().compareTo(precedence) >= 0) {
				break;
			}
			take();
			ReturnsValue nextvalue = getNextExpression(context,
					nextOperator.type.getPrecedence());
			OperatorTypes operationtype = ((OperatorToken) next).type;
			DeclaredType type1 = nextTerm.get_type(context).declType;
			DeclaredType type2 = nextvalue.get_type(context).declType;
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

	public ReturnsValue getNextTerm(ExpressionContext context)
			throws ParsingException {
		Token next = take();
		if (next instanceof ParenthesizedToken) {
			return ((ParenthesizedToken) next).get_single_value(context);
		} else if (next instanceof ValueToken) {
			return new ConstantAccess(((ValueToken) next).getValue(),
					next.lineInfo);
		} else if (next instanceof WordToken) {
			WordToken name = ((WordToken) next);
			next = peek();

			if (next instanceof ParenthesizedToken) {
				List<ReturnsValue> arguments = ((ParenthesizedToken) take())
						.get_arguments_for_call(context);
				return FunctionCall.generate_function_call(name, arguments,
						context);
			} else if (context.functionExists(name.name)) {
				return FunctionCall.generate_function_call(name,
						new ArrayList<ReturnsValue>(0), context);
			} else if (context.getConstantDefinition(name.name) != null) {
				return new ConstantAccess(context.getConstantDefinition(
						name.name).getValue(), name.lineInfo);
			}
			VariableAccess result = new VariableAccess(get_next_var_identifier(
					context, name), name.lineInfo);
			if (result.get_type(context) == null) {
				throw new NoSuchFunctionOrVariableException(name.lineInfo,
						name.name);
			}
			return result;

		} else {
			throw new UnrecognizedTokenException(next);
		}
	}

	public ReturnsValue getNextExpression(ExpressionContext context)
			throws ParsingException {
		return getNextExpression(context, precedence.NoPrecedence);
	}

	public VariableIdentifier get_next_var_identifier(
			ExpressionContext context, WordToken initial)
			throws ParsingException {
		VariableIdentifier identifier = new VariableIdentifier(initial.lineInfo);
		identifier.add(new String_SubvarIdentifier(initial.name));
		DeclaredType type = identifier.get_type(context).declType;
		while (true) {
			SubvarIdentifier s;
			if (peek() instanceof PeriodToken) {
				take();

				if (peek() instanceof WordToken) {
					s = new String_SubvarIdentifier(get_word_value().name);
				} else {
					break;
				}
			} else if (peek() instanceof BracketedToken) {
				int offset = 0;
				if (type instanceof ArrayType) {
					offset = ((ArrayType) type).bounds.lower;
				}
				s = new ReturnsValue_SubvarIdentifier(
						((BracketedToken) take()).getNextExpression(context),
						offset);
			} else {
				break;
			}
			type = s.getType(type);
			identifier.add(s);
		}
		return identifier;
	}

	public VariableIdentifier get_next_var_identifier(ExpressionContext context)
			throws ParsingException {
		Token initial = take();
		if (!(initial instanceof WordToken)) {
			throw new ExpectedTokenException("[Variable name]", initial);
		}
		return get_next_var_identifier(context, (WordToken) initial);
	}

	public List<VariableDeclaration> get_variable_declarations(
			ExpressionContext context) throws ParsingException {
		List<VariableDeclaration> result = new ArrayList<VariableDeclaration>();
		/*
		 * reusing it, so it is further out of scope than necessary
		 */
		List<WordToken> names = new ArrayList<WordToken>();
		Token next;
		do {
			do {
				next = take();
				if (!(next instanceof WordToken)) {
					throw new ExpectedTokenException("[Variable Identifier]",
							next);
				}
				names.add((WordToken) next);
				next = take();
			} while (next instanceof CommaToken);
			if (!(next instanceof ColonToken)) {
				throw new ExpectedTokenException(":", next);
			}
			DeclaredType type;
			type = get_next_pascal_type(context);
			assert_next_semicolon();
			for (WordToken s : names) {
				VariableDeclaration v = new VariableDeclaration(s.name, type,
						s.lineInfo);
				String n = s.name;
				if (context.functionExists(n)) {
					throw new SameNameException(s.lineInfo, context
							.getCallableFunctions(n).get(0), v, n);
				} else if (context.getVariableDefinition(n) != null) {
					throw new SameNameException(s.lineInfo,
							context.getVariableDefinition(n), v, s.name);
				} else if (context.getConstantDefinition(s.name) != null) {
					throw new SameNameException(s.lineInfo,
							context.getConstantDefinition(n), v, s.name);
				} else {
					result.add(v);
				}
			}
			names.clear(); // reusing the list object
			next = peek();
		} while (next instanceof WordToken);
		return result;
	}

	public ReturnsValue get_single_value(ExpressionContext context)
			throws ParsingException {
		ReturnsValue result = getNextExpression(context);
		if (hasNext()) {
			Token next = take();
			throw new ExpectedTokenException(getClosingText(), next);
		}
		return result;
	}

	public Executable get_next_command(ExpressionContext context)
			throws ParsingException {
		Token next = take();
		LineInfo initialline = next.lineInfo;
		if (next instanceof IfToken) {
			ReturnsValue condition = getNextExpression(context);
			next = take();
			assert (next instanceof ThenToken);
			Executable command = get_next_command(context);
			Executable else_command = null;
			next = peek();
			if (next instanceof ElseToken) {
				take();
				else_command = get_next_command(context);
			}
			return new IfStatement(condition, command, else_command,
					initialline);
		} else if (next instanceof WhileToken) {
			ReturnsValue condition = getNextExpression(context);
			next = take();
			assert (next instanceof DoToken);
			Executable command = get_next_command(context);
			return new WhileStatement(condition, command, initialline);
		} else if (next instanceof BeginEndToken) {
			InstructionGrouper begin_end_preprocessed = new InstructionGrouper(
					initialline);
			BeginEndToken cast_token = (BeginEndToken) next;
			if (cast_token.hasNext()) {

			}
			while (cast_token.hasNext()) {
				begin_end_preprocessed.add_command(cast_token
						.get_next_command(context));
				if (cast_token.hasNext()) {
					cast_token.assert_next_semicolon();
				}
			}
			return begin_end_preprocessed;
		} else if (next instanceof ForToken) {
			VariableIdentifier tmp_var = get_next_var_identifier(context);
			next = take();
			assert (next instanceof AssignmentToken);
			ReturnsValue first_value = getNextExpression(context);
			next = take();
			boolean downto = false;
			if (next instanceof DowntoToken) {
				downto = true;
			} else if (!(next instanceof ToToken)) {
				throw new ExpectedTokenException("[To] or [Downto]", next);
			}
			ReturnsValue last_value = getNextExpression(context);
			next = take();
			assert (next instanceof DoToken);
			Executable result;
			if (downto) { // TODO probably should merge these two types
				result = new DowntoForStatement(tmp_var, first_value,
						last_value, get_next_command(context), initialline);
			} else {
				result = new ForStatement(tmp_var, first_value, last_value,
						get_next_command(context), initialline);
			}
			return result;
		} else if (next instanceof RepeatToken) {
			InstructionGrouper command = new InstructionGrouper(initialline);

			while (!(peek_no_EOF() instanceof UntilToken)) {
				command.add_command(get_next_command(context));
				if (!(peek_no_EOF() instanceof UntilToken)) {
					assert_next_semicolon();
				}
			}
			next = take();
			if (!(next instanceof UntilToken)) {
				throw new ExpectedTokenException("until", next);
			}
			ReturnsValue condition = getNextExpression(context);
			return new RepeatInstruction(command, condition, initialline);
		} else if (next instanceof WordToken) {

			WordToken nametoken = (WordToken) next;
			next = peek();
			if (next instanceof ParenthesizedToken) {
				next = take();
				List<ReturnsValue> arguments = ((ParenthesizedToken) next)
						.get_arguments_for_call(context);
				return FunctionCall.generate_function_call(nametoken,
						arguments, context);
			} else if (next instanceof SemicolonToken
					|| next instanceof EOF_Token) {
				List<ReturnsValue> arguments = new ArrayList<ReturnsValue>();
				return FunctionCall.generate_function_call(nametoken,
						arguments, context);
			} else {
				// at this point assuming it is a variable identifier.
				VariableIdentifier identifier = get_next_var_identifier(
						context, nametoken);
				next = take();
				if (!(next instanceof AssignmentToken)) {
					throw new ExpectedTokenException(":=", next);
				}
				ReturnsValue value_to_assign = getNextExpression(context);
				DeclaredType output_type = identifier.get_type(context).declType;
				DeclaredType input_type = value_to_assign.get_type(context).declType;
				/*
				 * Does not have to be writable to assign value to variable.
				 */
				value_to_assign = output_type.convert(value_to_assign, context);
				if (value_to_assign == null) {
					throw new UnconvertableTypeException(next.lineInfo,
							input_type, output_type);
				}
				return new VariableSet(identifier, value_to_assign, initialline);
			}
		} else if (next instanceof CaseToken) {
			return new CaseInstruction((CaseToken) next, context);
		} else if (next instanceof SemicolonToken) {
			return new NopInstruction(next.lineInfo);
		}
		return context.root().handleUnrecognizedToken(next, this);
	}

	protected abstract String getClosingText();
}
