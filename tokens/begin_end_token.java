package tokens;

import java.util.LinkedList;

public class begin_end_token implements token, grouper_token {
	public LinkedList<token> insides = new LinkedList<token>();

	public void add_token(token token) {
		insides.add(token);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("begin ");
		for (token t : insides) {
			builder.append(t).append(' ');
		}
		builder.append("end ");
		return builder.toString();
	}
}
