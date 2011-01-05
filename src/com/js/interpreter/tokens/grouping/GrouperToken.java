package com.js.interpreter.tokens.grouping;

import java.util.concurrent.LinkedBlockingQueue;

import com.js.interpreter.exceptions.ExpectedAnotherTokenException;
import com.js.interpreter.exceptions.GroupingException;
import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.tokens.EOF_Token;
import com.js.interpreter.tokens.GroupingExceptionToken;
import com.js.interpreter.tokens.Token;

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
		if (next instanceof GroupingExceptionToken) {
			throw ((GroupingExceptionToken) next).exception;
		}
		return next;
	}

	public GrouperToken(LineInfo line) {
		super(line);
		queue = new LinkedBlockingQueue<Token>();
	}

	public boolean hasNext() throws GroupingException {
		return !(get_next() instanceof EOF_Token);
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
			} catch (InterruptedException e) {
				continue;
			}
			break;
		}
		return result;
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

}
