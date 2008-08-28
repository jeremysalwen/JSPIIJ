package tokens;

import java.util.LinkedList;

public abstract class grouper_token implements token {
	LinkedList<token> tokens = new LinkedList<token>();

	public void add_token(token g) {
		tokens.offer(g);
	}

	public token get_next_token() {
		token result=tokens.poll();
		while(result==null) {
			try {
				wait();
			} catch (InterruptedException e) {
			}
			result=tokens.poll();
		}
		return result;
	}
	public token get_next_token_no_EOF() {
		token result=get_next_token();
		if(result instanceof EOF_token) {
			System.err.println("Unexpected end of token group/file");
			System.err.println("Terminating program");
			System.exit(1);
		}
		return result;
	}
	public void push_back(token t) {
		tokens.push(t);
	}
	public token peek_next_token() {
		token result= tokens.peek();
		while(result==null) {
			try {
				wait();
			} catch(InterruptedException e) {
			}
			result=tokens.peek();
		}
		return result;
	}
	public token peek_next_token_no_EOF() {
		token result=peek_next_token();
		if(result instanceof EOF_token) {
			System.err.println("Unexpected end of token group/file");
			System.err.println("Terminating program");
			System.exit(1);
		}
		return result;
	}
	public boolean has_next() {
		return !(peek_next_token() instanceof EOF_token);
	}
}
