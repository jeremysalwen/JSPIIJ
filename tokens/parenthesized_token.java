package tokens;

import java.util.LinkedList;
import java.util.Queue;

public class parenthesized_token implements grouper_token, token {
	public Queue<token> insides = new LinkedList<token>();

	public void add_token(token t) {
		insides.add(t);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("(");
		for (token t : insides) {
			builder.append(t).append(' ');
		}
		builder.append(')');
		return builder.toString();
	}
}
