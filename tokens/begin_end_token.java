package tokens;

import java.util.LinkedList;
import java.util.Vector;

public class begin_end_token implements token, grouper_token {
	public LinkedList<token> insides = new LinkedList<token>();

	public void add_token(token token) {
		insides.add(token);
	}
}
