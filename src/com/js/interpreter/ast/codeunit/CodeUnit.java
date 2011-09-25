package com.js.interpreter.ast.codeunit;

import java.io.Reader;
import java.util.List;

import com.google.common.collect.ListMultimap;
import com.js.interpreter.ast.AbstractFunction;
import com.js.interpreter.ast.expressioncontext.ExpressionContextContract;
import com.js.interpreter.ast.expressioncontext.ExpressionContextMixin;
import com.js.interpreter.ast.instructions.Executable;
import com.js.interpreter.exceptions.ExpectedTokenException;
import com.js.interpreter.exceptions.ParsingException;
import com.js.interpreter.exceptions.UnrecognizedTokenException;
import com.js.interpreter.runtime.codeunit.RuntimeCodeUnit;
import com.js.interpreter.startup.ScriptSource;
import com.js.interpreter.tokenizer.NewLexer;
import com.js.interpreter.tokens.Token;
import com.js.interpreter.tokens.basic.ProgramToken;
import com.js.interpreter.tokens.grouping.BaseGrouperToken;
import com.js.interpreter.tokens.grouping.GrouperToken;

public abstract class CodeUnit implements ExpressionContextContract {
	public ExpressionContextMixin declarations;
	String program_name;

	public CodeUnit(ListMultimap<String, AbstractFunction> functionTable) {
		declarations = new ExpressionContextMixin(this, null, functionTable);
		prepareForParsing();

	}

	public CodeUnit(Reader program,
			ListMultimap<String, AbstractFunction> functionTable,
			String sourcename, List<ScriptSource> includeDirectories)
			throws ParsingException {
		this(functionTable);

		NewLexer grouper = new NewLexer(program, sourcename, includeDirectories);
		new Thread(grouper).start();
		parse_tree(grouper.token_queue);
	}

	void parse_tree(GrouperToken tokens) throws ParsingException {
		while (tokens.hasNext()) {
			declarations.add_next_declaration(tokens);
		}
	}

	protected void handleBeginEnd(BaseGrouperToken i)
			throws ExpectedTokenException, ParsingException {
		i.take();
	}

	protected void prepareForParsing() {
		return;
	}

	@Override
	public Executable handleUnrecognizedStatement(Token next,
			GrouperToken container) throws ParsingException {
		throw new UnrecognizedTokenException(next);
	}

	@Override
	public boolean handleUnrecognizedDeclaration(Token next, GrouperToken i)
			throws ParsingException {
		if (next instanceof ProgramToken) {
			this.program_name = i.next_word_value();
			i.assert_next_semicolon();
			return true;
		}
		return false;
	}

	public abstract RuntimeCodeUnit<? extends CodeUnit> run();

}
