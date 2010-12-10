package com.js.interpreter.tokens.grouping;

import java.util.concurrent.LinkedBlockingQueue;

import com.js.interpreter.exceptions.ExpectedAnotherTokenException;
import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.tokens.EOF_Token;
import com.js.interpreter.tokens.Token;

public abstract class GrouperToken extends Token {
	LinkedBlockingQueue<Token> queue;

	Token next = null;

	private Token get_next() {
		if (next == null) {
			try {
				next = queue.take();
			} catch (InterruptedException e) {
				System.err
						.println("Interrupted while attempting to acess queue for first time.");
				e.printStackTrace();
			}
		}
		return next;
	}

	public GrouperToken(LineInfo line) {
		super(line);
		queue = new LinkedBlockingQueue<Token>();
	}

	public boolean hasNext() {
		return !(get_next() instanceof EOF_Token);
	}

	public void put(Token t) {
		try {
			queue.put(t);
		} catch (InterruptedException e) {
			System.err
					.println("Interrupted while attempting to insert object into queue.");
			e.printStackTrace();
		}
	}

	public Token take() throws ExpectedAnotherTokenException {
		Token result = get_next();
		if (result instanceof EOF_Token) {
			throw new ExpectedAnotherTokenException(result.lineInfo);
		}
		try {
			next = queue.take();
		} catch (InterruptedException e) {
			System.err.println("Interrupted while taking from grouping_token");
			e.printStackTrace();
		}
		return result;
	}

	public Token peek() {
		return get_next();
	}

	public Token peek_no_EOF() throws ExpectedAnotherTokenException {
		Token result = peek();
		if (result instanceof EOF_Token) {
			throw new ExpectedAnotherTokenException(result.lineInfo);
		}
		return result;
	}

	@Override
	public String toString() {
		return get_next().toString() + ',' + queue.toString();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -5736870403548847904L;

}
