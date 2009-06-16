package edu.js.interpreter.tokens.grouping;

import java.util.concurrent.LinkedBlockingQueue;

import edu.js.interpreter.tokens.EOF_token;
import edu.js.interpreter.tokens.token;


public class grouper_token implements token {
	LinkedBlockingQueue<token> queue;

	token next = null;

	private token get_next() {
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

	public grouper_token() {
		queue = new LinkedBlockingQueue<token>();
	}

	public boolean hasNext() {
		return !(get_next() instanceof EOF_token);
	}

	public void put(token t) {
		try {
			queue.put(t);
		} catch (InterruptedException e) {
			System.err
					.println("Interrupted while attempting to insert object into queue.");
			e.printStackTrace();
		}
	}

	public token take() {
		token result = get_next();
		if (result instanceof EOF_token) {
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

	public token peek() {
		return get_next();
	}

	public token peek_no_EOF() {
		token result = peek();
		if (result instanceof EOF_token) {
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
