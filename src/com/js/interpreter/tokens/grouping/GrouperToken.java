package com.js.interpreter.tokens.grouping;

import com.js.interpreter.ast.VariableDeclaration;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.instructions.Assignment;
import com.js.interpreter.ast.instructions.Executable;
import com.js.interpreter.ast.instructions.InstructionGrouper;
import com.js.interpreter.ast.instructions.NopInstruction;
import com.js.interpreter.ast.instructions.case_statement.CaseInstruction;
import com.js.interpreter.ast.instructions.conditional.*;
import com.js.interpreter.ast.returnsvalue.*;
import com.js.interpreter.ast.returnsvalue.operators.BinaryOperatorEvaluation;
import com.js.interpreter.exceptions.*;
import com.js.interpreter.exceptions.grouping.GroupingException;
import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.pascaltypes.*;
import com.js.interpreter.tokens.*;
import com.js.interpreter.tokens.basic.*;
import com.js.interpreter.tokens.value.ValueToken;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

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
        Token n = take();
        if (n instanceof ArrayToken) {
            return getArrayType(context);
        }
        if (n instanceof RecordToken) {
            RecordToken r = (RecordToken) n;
            RecordType result = new RecordType();
            result.variable_types = r.get_variable_declarations(context);
            return result;
        }
        if (n instanceof OperatorToken && ((OperatorToken)n).type == OperatorTypes.DEREF) {
            DeclaredType pointed_type = get_next_pascal_type(context);
            return new PointerType(pointed_type);
        }
        /*if (n instanceof ClassToken) {
			ClassToken o = (ClassToken)n;
			ClassType result = new ClassType();
			throw new ExpectedTokenException("[asdf]", n);
		}*/
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
            DeclaredType elementType = get_next_pascal_type(context);
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
            Token t = bounds.take();
            if (!(t instanceof CommaToken)) {
                throw new ExpectedTokenException("']' or ','", t);
            }
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

    public RValue getNextExpression(ExpressionContext context,
                                    precedence precedence, Token next) throws ParsingException {
        RValue nextTerm;
        if (next instanceof OperatorToken) {
            OperatorToken nextOperator = (OperatorToken) next;
            if (!nextOperator.can_be_unary() || nextOperator.postfix()) {
                throw new BadOperationTypeException(next.lineInfo,
                        nextOperator.type);
            }
            nextTerm = UnaryOperatorEvaluation.generateOp(context, getNextExpression(context, nextOperator.type.getPrecedence()), nextOperator.type,  nextOperator.lineInfo);
        } else {
            nextTerm = getNextTerm(context, next);
        }
        while ((next = peek()).getOperatorPrecedence() != null) {
            if (next instanceof OperatorToken) {
                OperatorToken nextOperator = (OperatorToken) next;
                if (nextOperator.type.getPrecedence().compareTo(precedence) >= 0) {
                    break;
                }
                take();
                if(nextOperator.postfix()) {
                    return UnaryOperatorEvaluation.generateOp(context, nextTerm, nextOperator.type, nextOperator.lineInfo);
                }
                RValue nextvalue = getNextExpression(context,
                        nextOperator.type.getPrecedence());
                OperatorTypes operationtype = ((OperatorToken) next).type;
                DeclaredType type1 = nextTerm.get_type(context).declType;
                DeclaredType type2 = nextvalue.get_type(context).declType;
                try {
                    operationtype.verifyBinaryOperation(type1, type2);
                } catch (BadOperationTypeException e) {
                    throw new BadOperationTypeException(next.lineInfo, type1,
                            type2, nextTerm, nextvalue, operationtype);
                }
                nextTerm = BinaryOperatorEvaluation.generateOp(context,
                        nextTerm, nextvalue, operationtype,
                        nextOperator.lineInfo);
            } else if (next instanceof PeriodToken) {
                take();
                next = take();
                if (!(next instanceof WordToken)) {
                    throw new ExpectedTokenException("[Element Name]", next);
                }
                nextTerm = new FieldAccess(nextTerm, (WordToken) next);
            } else if (next instanceof BracketedToken) {
                take();
                BracketedToken b = (BracketedToken) next;
                RuntimeType t = nextTerm.get_type(context);
                RValue v = b.getNextExpression(context);
                RValue converted = BasicType.Integer.convert(v, context);
                if (converted == null) {
                    throw new NonIntegerIndexException(v);
                }
                if (b.hasNext()) {
                    throw new ExpectedTokenException("]", b.take());
                }
                nextTerm = t.declType.generateArrayAccess(nextTerm, converted);
            }
        }
        return nextTerm;
    }

    public RValue getNextExpression(ExpressionContext context,
                                    precedence precedence) throws ParsingException {
        return getNextExpression(context, precedence, take());
    }

    public RValue getNextTerm(ExpressionContext context, Token next)
            throws ParsingException {
        if (next instanceof ParenthesizedToken) {
            return ((ParenthesizedToken) next).get_single_value(context);
        } else if (next instanceof ValueToken) {
            return new ConstantAccess(((ValueToken) next).getValue(),
                    next.lineInfo);
        } else if (next instanceof WordToken) {
            WordToken name = ((WordToken) next);
            next = peek();

            if (next instanceof ParenthesizedToken) {
                List<RValue> arguments = ((ParenthesizedToken) take())
                        .get_arguments_for_call(context);
                return FunctionCall.generate_function_call(name, arguments,
                        context);
            } else {
                return context.getIdentifierValue(name);
            }
        } else {
            throw new UnrecognizedTokenException(next);
        }
    }

    public RValue getNextTerm(ExpressionContext context)
            throws ParsingException {
        return getNextTerm(context, take());
    }

    public RValue getNextExpression(ExpressionContext context)
            throws ParsingException {
        return getNextExpression(context, precedence.NoPrecedence);
    }

    public RValue getNextExpression(ExpressionContext context, Token first)
            throws ParsingException {
        return getNextExpression(context, precedence.NoPrecedence, first);
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

            Object defaultValue = null;
            if (peek() instanceof OperatorToken) {
                if (((OperatorToken) peek()).type == OperatorTypes.EQUALS) {
                    take();
                    RValue unconverted = getNextExpression(context);
                    RValue converted = type.convert(unconverted, context);
                    if (converted == null) {
                        throw new UnconvertibleTypeException(unconverted,
                                unconverted.get_type(context).declType, type,
                                true);
                    }
                    defaultValue = converted.compileTimeValue(context);
                    if (defaultValue == null) {
                        throw new NonConstantExpressionException(converted);
                    }
                    if (names.size() != 1) {
                        throw new MultipleDefaultValuesException(
                                converted.getLineNumber());
                    }
                }
            }
            assert_next_semicolon();
            for (WordToken s : names) {
                VariableDeclaration v = new VariableDeclaration(s.name, type,
                        defaultValue, s.lineInfo);
                context.verifyNonConflictingSymbol(v);
                result.add(v);
            }
            names.clear(); // reusing the list object
            next = peek();
        } while (next instanceof WordToken);
        return result;
    }

    public RValue get_single_value(ExpressionContext context)
            throws ParsingException {
        RValue result = getNextExpression(context);
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
            RValue condition = getNextExpression(context);
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
            RValue condition = getNextExpression(context);
            next = take();
            assert (next instanceof DoToken);
            Executable command = get_next_command(context);
            return new WhileStatement(condition, command, initialline);
        } else if (next instanceof BeginEndToken) {
            InstructionGrouper begin_end_preprocessed = new InstructionGrouper(
                    initialline);
            BeginEndToken cast_token = (BeginEndToken) next;

            while (cast_token.hasNext()) {
                begin_end_preprocessed.add_command(cast_token
                        .get_next_command(context));
                if (cast_token.hasNext()) {
                    cast_token.assert_next_semicolon();
                }
            }
            return begin_end_preprocessed;
        } else if (next instanceof ForToken) {
            RValue tmp_val = getNextExpression(context);
            LValue tmp_var = tmp_val.asLValue(context);
            if(tmp_var == null) {
                throw new UnassignableTypeException(tmp_val);
            }
            next = take();
            assert (next instanceof AssignmentToken);
            RValue first_value = getNextExpression(context);
            next = take();
            boolean downto = false;
            if (next instanceof DowntoToken) {
                downto = true;
            } else if (!(next instanceof ToToken)) {
                throw new ExpectedTokenException("[To] or [Downto]", next);
            }
            RValue last_value = getNextExpression(context);
            next = take();
            assert (next instanceof DoToken);
            Executable result;
            if (downto) { // TODO probably should merge these two types
                result = new DowntoForStatement(context, tmp_var, first_value,
                        last_value, get_next_command(context), initialline);
            } else {
                result = new ForStatement(context, tmp_var, first_value,
                        last_value, get_next_command(context), initialline);
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
            RValue condition = getNextExpression(context);
            return new RepeatInstruction(command, condition, initialline);
        } else if (next instanceof CaseToken) {
            return new CaseInstruction((CaseToken) next, context);
        } else if (next instanceof SemicolonToken) {
            return new NopInstruction(next.lineInfo);
        } else {
            try {
                return context.handleUnrecognizedStatement(next, this);
            } catch (ParsingException e) {
            }
            RValue r = getNextExpression(context, next);
            next = peek();
            if (next instanceof AssignmentToken) {
                take();
                LValue left = r.asLValue(context);
                if(left == null) {
                    throw new UnassignableTypeException(r);
                }
                RValue value_to_assign = getNextExpression(context);
                DeclaredType output_type = left.get_type(context).declType;
                DeclaredType input_type = value_to_assign.get_type(context).declType;
				/*
				 * Does not have to be writable to assign value to variable.
				 */
                RValue converted = output_type.convert(value_to_assign,
                        context);
                if (converted == null) {
                    throw new UnconvertibleTypeException(value_to_assign,
                            input_type, output_type, true);
                }
                return new Assignment(left, output_type
                        .cloneValue(converted), next.lineInfo);
            } else if (r instanceof Executable) {
                return (Executable) r;
            } else {
                throw new NotAStatementException(r);
            }

        }
    }

    protected abstract String getClosingText();

}
