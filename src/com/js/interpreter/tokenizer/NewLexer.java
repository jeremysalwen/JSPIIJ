package com.js.interpreter.tokenizer;

import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.List;
import java.util.Stack;

import com.js.interpreter.exceptions.grouping.EnumeratedGroupingException;
import com.js.interpreter.exceptions.grouping.EnumeratedGroupingException.grouping_exception_types;
import com.js.interpreter.exceptions.grouping.GroupingException;
import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.startup.ScriptSource;
import com.js.interpreter.tokens.EOF_Token;
import com.js.interpreter.tokens.GroupingExceptionToken;
import com.js.interpreter.tokens.Token;
import com.js.interpreter.tokens.WarningToken;
import com.js.interpreter.tokens.closing.EndBracketToken;
import com.js.interpreter.tokens.closing.EndParenToken;
import com.js.interpreter.tokens.closing.EndToken;
import com.js.interpreter.tokens.grouping.BaseGrouperToken;
import com.js.interpreter.tokens.grouping.BeginEndToken;
import com.js.interpreter.tokens.grouping.BracketedToken;
import com.js.interpreter.tokens.grouping.CaseToken;
import com.js.interpreter.tokens.grouping.GrouperToken;
import com.js.interpreter.tokens.grouping.ParenthesizedToken;
import com.js.interpreter.tokens.grouping.RecordToken;

public class NewLexer implements Runnable {
	public BaseGrouperToken token_queue;
	Stack<GrouperToken> groupers;
	Lexer lexer;

	public NewLexer(Reader reader, String sourcename,
			List<ScriptSource> searchDirectories) throws GroupingException {
		this.lexer = new Lexer(reader, sourcename, searchDirectories);
		groupers = new Stack<GrouperToken>();
		token_queue = new BaseGrouperToken(new LineInfo(0, sourcename));
		groupers.push(token_queue);
	}

	void TossException(GroupingException e) {
		GroupingExceptionToken t = new GroupingExceptionToken(e);
		for (GrouperToken g : groupers) {
			g.put(t);
		}
	}

	public void parse() {
		while (true) {
			GrouperToken top_of_stack = groupers.peek();
			try {
				Token t = lexer.yylex();

				if (t instanceof EndParenToken) {
					if (top_of_stack instanceof ParenthesizedToken) {
						top_of_stack.put(new EOF_Token(t.lineInfo));
						groupers.pop();
						continue;
					} else {
						TossException(new EnumeratedGroupingException(
								t.lineInfo,
								grouping_exception_types.MISMATCHED_PARENS));
						return;
					}
				}
				if (t instanceof EndBracketToken) {
					if (top_of_stack instanceof BracketedToken) {
						top_of_stack.put(new EOF_Token(t.lineInfo));
						groupers.pop();
						continue;
					} else {
						TossException(new EnumeratedGroupingException(
								t.lineInfo,
								grouping_exception_types.MISMATCHED_BRACKETS));
						return;
					}
				}
				if (t instanceof EndToken) {
					if (top_of_stack instanceof BeginEndToken
							|| top_of_stack instanceof RecordToken
							|| top_of_stack instanceof CaseToken) {
						top_of_stack.put(new EOF_Token(t.lineInfo));
						groupers.pop();
						continue;
					} else {
						TossException(new EnumeratedGroupingException(
								t.lineInfo, grouping_exception_types.EXTRA_END));
						return;
					}
				}
				if (t instanceof WarningToken) {
					// TODO handle warnings...
					continue;
				}
				if (t instanceof EOF_Token) {
					if (groupers.size() != 1) {
						if (top_of_stack instanceof ParenthesizedToken) {
							TossException(new EnumeratedGroupingException(
									top_of_stack.lineInfo,
									grouping_exception_types.UNFINISHED_PARENS));
						} else if (top_of_stack instanceof BeginEndToken) {
							TossException(new EnumeratedGroupingException(
									top_of_stack.lineInfo,
									grouping_exception_types.UNFINISHED_BEGIN_END));
						} else {
							TossException(new EnumeratedGroupingException(
									top_of_stack.lineInfo,
									grouping_exception_types.UNFINISHED_CONSTRUCT));
						}
					} else {
						top_of_stack.put(t);
					}
					return;
				}
				// Everything else passes through normally.
				top_of_stack.put(t);
				if (t instanceof GrouperToken) {
					groupers.push((GrouperToken) t);
				}
			} catch (IOException e) {
				EnumeratedGroupingException g = new EnumeratedGroupingException(
						top_of_stack.lineInfo,
						grouping_exception_types.IO_EXCEPTION);
				g.caused = e;
				TossException(g);
				return;
			}
		}
	}

	@Override
	public void run() {
		parse();
	}
}
