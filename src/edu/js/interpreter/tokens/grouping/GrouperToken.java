package edu.js.interpreter.tokens.grouping;

import java.util.concurrent.LinkedBlockingQueue;

import edu.js.interpreter.tokens.EOF_Token;
import edu.js.interpreter.tokens.Token;


public class GrouperToken implements Token {
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

	public GrouperToken() {
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

	public Token take() {
		Token result = get_next();
		if (result instanceof EOF_Token) {
			System.err.println("Unexpected end of token queue in take");
			System.exit(1);
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

	public Token peek_no_EOF() {
		Token result = peek();
		if (result instanceof EOF_Token) {
			System.err.println("Unexpected end of token queue in peek");
			System.exit(1);
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
