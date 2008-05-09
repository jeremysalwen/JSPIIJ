package tokens;

import java.util.LinkedList;

public class parenthesized_token implements grouper_token, token {
	public LinkedList<token> insides = new LinkedList<token>();

	public void add_token(token t) {
		insides.add(t);
	}
}
