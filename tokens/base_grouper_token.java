package tokens;

import java.util.ArrayList;
import java.util.List;

public class base_grouper_token implements grouper_token {
	public List<token> tokens = new ArrayList<token>();

	public void add_token(token g) {
		tokens.add(g);
	}

}
