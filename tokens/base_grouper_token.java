package tokens;

import java.util.LinkedList;

public class base_grouper_token implements grouper_token {
	public LinkedList<token> tokens = new LinkedList<token>();

	@Override
	public void add_token(token g) {
		tokens.add(g);
	}

}
