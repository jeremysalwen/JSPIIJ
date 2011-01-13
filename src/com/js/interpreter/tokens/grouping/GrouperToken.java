package com.js.interpreter.tokens.grouping;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import com.js.interpreter.ast.ExpressionContext;
import com.js.interpreter.ast.VariableDeclaration;
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
import com.js.interpreter.exceptions.NonIntegerIndexException;
import com.js.interpreter.exceptions.ParsingException;
import com.js.interpreter.exceptions.UnrecognizedTokenException;
import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.pascaltypes.ArrayType;
import com.js.interpreter.pascaltypes.DeclaredType;
import com.js.interpreter.pascaltypes.JavaClassBasedType;
import com.js.interpreter.pascaltypes.RuntimeType;
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
import com.js.interpreter.tokens.basic.ColonToken;
import com.js.interpreter.tokens.basic.CommaToken;
import com.js.interpreter.tokens.basic.OfToken;
import com.js.interpreter.tokens.basic.PeriodToken;
import com.js.interpreter.tokens.basic.SemicolonToken;
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
				s = new ReturnsValue_SubvarIdentifier(((BracketedToken) take())
						.getNextExpression(context), offset);
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
		List<String> names = new ArrayList<String>(1);
		Token next;
		do {
			do {
				names.add(next_word_value());
				next = take();
			} while (next instanceof CommaToken);
			if (!(next instanceof ColonToken)) {
				throw new ExpectedTokenException(":", next);
			}
			DeclaredType type;
			type = get_next_pascal_type(context);
			assert_next_semicolon();
			for (String s : names) {
				result.add(new VariableDeclaration(s, type));
				/*
				 * TODO make sure this conforms to pascal
				 */
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

	protected abstract String getClosingText();
}
